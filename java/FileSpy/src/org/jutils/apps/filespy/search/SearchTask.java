package org.jutils.apps.filespy.search;

import java.io.File;
import java.time.*;
import java.util.concurrent.TimeUnit;

import org.jutils.ValidationException;
import org.jutils.apps.filespy.data.SearchParams;
import org.jutils.apps.filespy.data.SearchRecord;
import org.jutils.concurrent.*;
import org.jutils.pattern.StringPattern.IMatcher;
import org.jutils.pattern.StringPattern.Match;
import org.jutils.ui.MessageExceptionView;

/*******************************************************************************
 *
 ******************************************************************************/
public class SearchTask implements IStoppableTask
{
    /**  */
    private static final ZoneId GMT = ZoneId.of( "GMT" );
    /**  */
    private final SearchResultsHandler searchHandler;
    /**  */
    private final SearchParams params;
    /**  */
    private final IMatcher filenamePattern;
    /**  */
    private final Runnable finalizer;

    /***************************************************************************
     * @param handler SearchHandler
     * @param params SearchParams
     **************************************************************************/
    public SearchTask( SearchResultsHandler handler, SearchParams params,
        Runnable finalizer )
    {
        IMatcher fm = null;
        try
        {
            fm = params.filename.createMatcher();
        }
        catch( ValidationException e )
        {
            fm = null;
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        this.searchHandler = handler;
        this.params = params;
        this.filenamePattern = fm;
        this.finalizer = finalizer;
    }

    /***************************************************************************
     *
     **************************************************************************/
    @Override
    public void run( ITaskStopManager stopper )
    {
        IResultsConsumer fileConsumer = null;

        if( params.contents.isUsed )
        {
            IMatcher contentsMatcher;
            try
            {
                contentsMatcher = params.contents.data.createMatcher();
                fileConsumer = new FileContentsConsumer( contentsMatcher,
                    searchHandler );
            }
            catch( ValidationException e )
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else
        {
            fileConsumer = new FileNameConsumer( searchHandler );
        }

        File [] searchPaths = new File[] { params.path };

        for( File searchFolder : searchPaths )
        {
            File [] files = searchFolder.listFiles();

            files = files == null ? new File[0] : files;

            for( File file : files )
            {
                findFiles( file, fileConsumer, stopper );
            }
        }

        // LogUtils.printDebug( "Found files. Awaiting contents search" );

        fileConsumer.signalInputFinished();

        // contentsSearcherThread.interrupt();

        finalizer.run();

        stopper.signalFinished();
    }

    /***************************************************************************
     * @param lastModified
     * @return
     **************************************************************************/
    private boolean isAfter( long lastModified )
    {
        if( params.after.isUsed )
        {
            Instant instant = Instant.ofEpochMilli( lastModified );
            ZonedDateTime zdt = ZonedDateTime.ofInstant( instant, GMT );

            return params.after.data.isBefore( zdt.toLocalDate() );
        }

        return true;
    }

    /***************************************************************************
     * @param lastModified
     * @return
     **************************************************************************/
    private boolean isBefore( long lastModified )
    {
        if( params.before.isUsed )
        {
            Instant instant = Instant.ofEpochMilli( lastModified );
            ZonedDateTime zdt = ZonedDateTime.ofInstant( instant, GMT );

            return params.before.data.isAfter( zdt.toLocalDate() );
        }

        return true;
    }

    /***************************************************************************
     * @param length
     * @return
     **************************************************************************/
    private boolean isLessThan( long length )
    {
        return !params.lessThan.isUsed || length > params.lessThan.data;
    }

    /***************************************************************************
     * @param length
     * @return
     **************************************************************************/
    private boolean isMoreThan( long length )
    {
        return !params.moreThan.isUsed || length > params.moreThan.data;
    }

    /***************************************************************************
     * @param file
     * @return
     **************************************************************************/
    private boolean testMetrics( File file )
    {
        boolean matched = false;

        // LogUtils.printDebug( "Testing file: %s", file.getAbsolutePath() );

        if( filenamePattern != null )
        {
            Match m = filenamePattern.find( file.getName() );
            matched = m.matches ^ params.filenameNot;

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
        else if( stopper.continueProcessing() && testMetrics( file ) )
        {
            SearchRecord record = new SearchRecord( file );

            if( !isDir || !params.contents.isUsed )
            {
                fileConsumer.consume( record, stopper );
            }
        }

        if( stopper.continueProcessing() && isDir && params.searchSubfolders )
        {
            File [] children = file.listFiles();

            if( children == null )
            {
                SearchResultsHandler.addErrorMessage(
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
        private final SafeExecutorService contentsService;

        public FileContentsConsumer( IMatcher contentsMatcher,
            SearchResultsHandler searchHandler )
        {
            this.searchHandler = searchHandler;
            this.contentsSearcher = new FileContentsSearcher( contentsMatcher,
                searchHandler );
            this.contentsService = new SafeExecutorService( 8,
                new IFinishedHandler()
                {
                    @Override
                    public void signalError( Throwable t )
                    {
                        MessageExceptionView.invokeLater( t, "ERROR",
                            "Error searching files" );
                    }

                    @Override
                    public void signalComplete()
                    {
                    }
                } );
        }

        @Override
        public void consume( SearchRecord record, ITaskStopManager stopper )
        {
            File file = record.getFile();

            if( file.canRead() )
            {
                contentsSearcher.addFile();
                // LogUtils.printDebug( "Found record for file " +
                // record.getFile().getAbsolutePath() );
                contentsService.submit(
                    () -> contentsSearcher.consume( record, stopper ) );
            }
            else
            {
                SearchResultsHandler.addErrorMessage(
                    "Cannot read file" + file.getAbsolutePath() );
            }
        }

        @Override
        public void signalInputFinished()
        {
            // LogUtils.printDebug( "Shutting contents service down" );
            contentsService.shutdown();

            // LogUtils.printDebug( "awaiting contents service termination" );
            try
            {
                while( !contentsService.awaitTermination( 1L,
                    TimeUnit.SECONDS ) )
                {
                    // LogUtils.printDebug( "still waiting for termination" );
                }
            }
            catch( InterruptedException ex )
            {
                // Ignore interrupt.
                // ex.printStackTrace();
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

        public FileNameConsumer( SearchResultsHandler searchHandler )
        {
            this.searchHandler = searchHandler;
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
        }
    }
}
