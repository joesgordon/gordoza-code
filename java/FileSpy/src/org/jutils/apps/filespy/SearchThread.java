package org.jutils.apps.filespy;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jutils.apps.filespy.data.SearchParams;
import org.jutils.apps.filespy.data.SearchRecord;
import org.jutils.concurrent.*;
import org.jutils.ui.event.IEventListener;

/*******************************************************************************
 *
 ******************************************************************************/
public class SearchThread implements IStoppable
{
    /**  */
    private final SearchHandler searchHandler;
    /**  */
    private final SearchParams params;
    /**  */
    private final Pattern filenamePattern;

    /***************************************************************************
     * @param handler SearchHandler
     * @param params SearchParams
     **************************************************************************/
    public SearchThread( SearchHandler handler, SearchParams params )
    {
        this.searchHandler = handler;
        this.params = params;
        this.filenamePattern = params.getFilenamePattern();
    }

    /***************************************************************************
     *
     **************************************************************************/
    @Override
    public void run( IStopper stopper )
    {
        FileContentsSearcher contentsSearcher = new FileContentsSearcher(
            params.getContentsPattern(), searchHandler );
        Consumer<SearchRecord> contentsConsumer = new Consumer<SearchRecord>(
            contentsSearcher );
        Stoppable searcher = new Stoppable( contentsConsumer );
        Thread contentsSearcherThread = new Thread( searcher );

        stopper.addStoppedListener( new StopListener( contentsConsumer,
            searcher, contentsSearcherThread ) );

        contentsSearcherThread.start();

        File[] searchPaths = params.getSearchFolders();

        for( File searchFolder : searchPaths )
        {
            File[] files = searchFolder.listFiles();

            for( File file : files )
            {
                findFiles( file, contentsConsumer, stopper );
            }
        }

        contentsConsumer.stopAcceptingInput();
        contentsSearcherThread.interrupt();

        try
        {
            searcher.stopAndWaitFor();
        }
        catch( InterruptedException e )
        {
            ;
        }

        searchHandler.updateStatus( "" );

        contentsSearcherThread = null;
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
            matched = true;
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
    private void findFiles( File file, Consumer<SearchRecord> contentsConsumer,
        IStopper stopper )
    {
        boolean isDir = file.isDirectory();

        if( isDir )
        {
            searchHandler.updateStatus( "Searching: " + file.getAbsolutePath() );
        }

        if( stopper.continueProcessing() && testMetrics( file ) )
        {
            SearchRecord record = new SearchRecord( file );

            if( isDir && !params.contentsMatch )
            {
                searchHandler.addFile( record );
            }
            else if( params.contents != null )
            {
                if( file.canRead() )
                {
                    contentsConsumer.addData( record );
                }
                else
                {
                    searchHandler.addErrorMessage( "Cannot read file" +
                        file.getAbsolutePath() );
                }
            }
            else
            {
                searchHandler.addFile( record );
            }
        }

        if( stopper.continueProcessing() && isDir && params.searchSubfolders )
        {
            File[] children = file.listFiles();

            if( children == null )
            {
                searchHandler.addErrorMessage( "Do not have permission to list " +
                    file.getAbsolutePath() );
            }
            else
            {
                for( File child : children )
                {
                    findFiles( child, contentsConsumer, stopper );
                }
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class StopListener implements IEventListener
    {
        private final Stoppable searcher;
        /**  */
        private final Consumer<SearchRecord> contentsConsumer;
        /**  */
        private final Thread contentsSearcherThread;

        public StopListener( Consumer<SearchRecord> contentsConsumer,
            Stoppable searcher, Thread contentsSearcherThread )
        {
            this.contentsConsumer = contentsConsumer;
            this.searcher = searcher;
            this.contentsSearcherThread = contentsSearcherThread;
        }

        @Override
        public void eventOccurred()
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
