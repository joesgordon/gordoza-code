package org.jutils.apps.filespy.search;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jutils.apps.filespy.data.SearchParams;
import org.jutils.apps.filespy.data.SearchRecord;
import org.jutils.concurrent.*;
import org.jutils.ui.event.ItemActionEvent;
import org.jutils.ui.event.ItemActionListener;

/*******************************************************************************
 *
 ******************************************************************************/
public class SearchTask implements IStoppableTask
{
    /**  */
    private final SearchResultsHandler searchHandler;
    /**  */
    private final SearchParams params;
    /**  */
    private final Pattern filenamePattern;
    /**  */
    private final Runnable finalizer;

    /***************************************************************************
     * @param handler SearchHandler
     * @param params SearchParams
     **************************************************************************/
    public SearchTask( SearchResultsHandler handler, SearchParams params,
        Runnable finalizer )
    {
        this.searchHandler = handler;
        this.params = params;
        this.filenamePattern = params.getFilenamePattern();
        this.finalizer = finalizer;
    }

    /***************************************************************************
     *
     **************************************************************************/
    @Override
    public void run( ITaskStopManager stopper )
    {
        IResultsConsumer fileConsumer = null;

        if( params.contentsMatch )
        {
            fileConsumer = new FileContentsConsumer(
                params.getContentsPattern(), searchHandler, finalizer,
                stopper );
        }
        else
        {
            fileConsumer = new FileNameConsumer( searchHandler, finalizer );
        }

        File [] searchPaths = params.getSearchFolders();

        for( File searchFolder : searchPaths )
        {
            File [] files = searchFolder.listFiles();

            files = files == null ? new File[0] : files;

            for( File file : files )
            {
                findFiles( file, fileConsumer, stopper );
            }
        }

        fileConsumer.signalInputFinished();

        // contentsSearcherThread.interrupt();

        stopper.signalFinished();
    }

    /***************************************************************************
     * @param lastModified
     * @return
     **************************************************************************/
    private boolean isAfter( long lastModified )
    {
        return params.after == null ||
            lastModified < params.after.getTime().getTime();
    }

    /***************************************************************************
     * @param lastModified
     * @return
     **************************************************************************/
    private boolean isBefore( long lastModified )
    {
        return params.before == null ||
            lastModified > params.before.getTime().getTime();
    }

    /***************************************************************************
     * @param length
     * @return
     **************************************************************************/
    private boolean isLessThan( long length )
    {
        return params.lessThan == null || length > params.lessThan.longValue();
    }

    /***************************************************************************
     * @param length
     * @return
     **************************************************************************/
    private boolean isMoreThan( long length )
    {
        return params.moreThan == null || length > params.moreThan.longValue();
    }

    /***************************************************************************
     * @param file
     * @return
     **************************************************************************/
    private boolean testMetrics( File file )
    {
        boolean matched = false;

        if( filenamePattern != null )
        {
            Matcher matcher = filenamePattern.matcher( file.getName() );
            matched = matcher.find() ^ params.filenameNot;

            if( params.filenameNot )
            {
                matched ^= true;
            }
        }
        else
        {
            // -----------------------------------------------------------------
            // Don't match directories if looking for contents:
            // +--------------------------+
            // | Dir | Contents | Matched |
            // | .0. | ....0... | ...1... |
            // | .0. | ....1... | ...1... |
            // | .1. | ....0... | ...1... |
            // | .1. | ....1... | ...0... |
            // +--------------------------+
            // -----------------------------------------------------------------
            matched = !file.isDirectory() || params.contents == null;
        }

        if( matched )
        {
            long fileLength = file.length();
            long lastModified = file.lastModified();

            matched &= isAfter( lastModified );
            matched &= isBefore( lastModified );

            matched &= isLessThan( fileLength );
            matched &= isMoreThan( fileLength );
        }

        return matched;
    }

    /***********************************************************************
     * @param file
     **********************************************************************/
    private void findFiles( File file, IResultsConsumer fileConsumer,
        ITaskStopManager stopper )
    {
        boolean isDir = file.isDirectory();

        // LogUtils.printDebug( "Searching file " + file.getName() );

        if( isDir )
        {
            searchHandler.updateStatus( "Finding: " + file.getAbsolutePath() );
        }

        if( stopper.continueProcessing() && testMetrics( file ) )
        {
            SearchRecord record = new SearchRecord( file );

            if( !isDir || !params.contentsMatch )
            {
                fileConsumer.consume( record, stopper );
            }
        }

        if( stopper.continueProcessing() && isDir && params.searchSubfolders )
        {
            File [] children = file.listFiles();

            if( children == null )
            {
                searchHandler.addErrorMessage(
                    "Do not have permission to list " +
                        file.getAbsolutePath() );
            }
            else
            {
                for( File child : children )
                {
                    findFiles( child, fileConsumer, stopper );
                }
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static interface IResultsConsumer extends IConsumer<SearchRecord>
    {
        void signalInputFinished();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FileContentsConsumer implements IResultsConsumer
    {
        private final SearchResultsHandler searchHandler;
        private final FileContentsSearcher contentsSearcher;
        private final ConsumerTask<SearchRecord> contentsConsumer;
        private final Stoppable contentsStopper;
        private final Thread contentsSearcherThread;

        public FileContentsConsumer( Pattern contentsPattern,
            SearchResultsHandler searchHandler, Runnable finalizer,
            ITaskStopManager stopper )
        {
            this.searchHandler = searchHandler;
            this.contentsSearcher = new FileContentsSearcher( contentsPattern,
                searchHandler );
            this.contentsConsumer = new ConsumerTask<SearchRecord>(
                contentsSearcher, finalizer );
            this.contentsStopper = new Stoppable( contentsConsumer );
            this.contentsSearcherThread = new Thread( contentsStopper,
                "FileSpy Contents Search Thread" );

            stopper.addFinishedListener( new SearchStoppedListener(
                contentsConsumer, contentsStopper, contentsSearcherThread ) );

            contentsSearcherThread.start();
        }

        @Override
        public void consume( SearchRecord record, ITaskStopManager stopper )
        {
            File file = record.getFile();
            if( file.canRead() )
            {
                // LogUtils.printDebug( "Found record for file " +
                // record.getFile().getAbsolutePath() );
                contentsConsumer.addData( record );
            }
            else
            {
                searchHandler.addErrorMessage(
                    "Cannot read file" + file.getAbsolutePath() );
            }
        }

        @Override
        public void signalInputFinished()
        {
            contentsConsumer.stopAcceptingInput();

            try
            {
                // LogUtils.printDebug( "Waiting for contents to finish" );
                contentsStopper.waitFor();
                // LogUtils.printDebug( "Contents search finished" );
            }
            catch( InterruptedException e )
            {
                ;
            }

            searchHandler.updateStatus( "" );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FileNameConsumer implements IResultsConsumer
    {
        private final SearchResultsHandler searchHandler;
        private final Runnable finalizer;

        public FileNameConsumer( SearchResultsHandler searchHandler,
            Runnable finalizer )
        {
            this.searchHandler = searchHandler;
            this.finalizer = finalizer;
        }

        @Override
        public void consume( SearchRecord record, ITaskStopManager stopper )
        {
            searchHandler.addFile( record );
        }

        @Override
        public void signalInputFinished()
        {
            searchHandler.updateStatus( "" );
            finalizer.run();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class SearchStoppedListener
        implements ItemActionListener<Boolean>
    {
        private final Stoppable searcher;
        /**  */
        private final ConsumerTask<SearchRecord> contentsConsumer;
        /**  */
        private final Thread contentsSearcherThread;

        public SearchStoppedListener(
            ConsumerTask<SearchRecord> contentsConsumer, Stoppable searcher,
            Thread contentsSearcherThread )
        {
            this.contentsConsumer = contentsConsumer;
            this.searcher = searcher;
            this.contentsSearcherThread = contentsSearcherThread;
        }

        @Override
        public void actionPerformed( ItemActionEvent<Boolean> event )
        {
            contentsConsumer.stopAcceptingInput();
            searcher.stop();

            if( contentsSearcherThread != null )
            {
                contentsSearcherThread.interrupt();
            }
        }
    }
}
