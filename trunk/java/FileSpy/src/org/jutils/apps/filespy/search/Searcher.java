package org.jutils.apps.filespy.search;

import org.jutils.Stopwatch;
import org.jutils.apps.filespy.data.SearchParams;
import org.jutils.apps.filespy.ui.SearchView;
import org.jutils.concurrent.Stoppable;
import org.jutils.ui.StatusBarPanel;
import org.jutils.ui.event.ItemActionEvent;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.explorer.ExplorerTable;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Searcher
{
    /**  */
    private final ExplorerTable resultsTable;
    /**  */
    private final SearchView panel;
    /**  */
    private final StatusBarPanel statusBar;

    /**  */
    private Stoppable stoppableTask;
    /**  */
    private Thread searchThread;

    /***************************************************************************
     * @param resultsTable
     * @param panel
     * @param statusBar
     **************************************************************************/
    public Searcher( ExplorerTable resultsTable, SearchView panel,
        StatusBarPanel statusBar )
    {
        this.resultsTable = resultsTable;
        this.panel = panel;
        this.statusBar = statusBar;
    }

    /***************************************************************************
     * @param params
     * @param finishedListener
     **************************************************************************/
    public void search( SearchParams params,
        ItemActionListener<Long> finishedListener )
    {
        Stopwatch stopwatch = new Stopwatch();
        ConsumerFinalizer finalizer = new ConsumerFinalizer( stopwatch,
            finishedListener );
        SearchResultsHandler handler = new SearchResultsHandler( panel,
            statusBar );

        statusBar.setText( "" );
        resultsTable.clearTable();

        SearchThread searchTask = new SearchThread( handler, params, finalizer );

        stoppableTask = new Stoppable( searchTask );
        searchThread = new Thread( stoppableTask );
        searchThread.start();
    }

    public void cancel()
    {
        stoppableTask.stop();
        searchThread.interrupt();

        try
        {
            stoppableTask.waitFor();
        }
        catch( InterruptedException ex )
        {
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ConsumerFinalizer implements Runnable
    {
        /**  */
        private final Stopwatch stopwatch;
        /**  */
        private final ItemActionListener<Long> finishedListener;

        public ConsumerFinalizer( Stopwatch stopwatch,
            ItemActionListener<Long> finishedListener )
        {
            this.stopwatch = stopwatch;
            this.finishedListener = finishedListener;
        }

        @Override
        public void run()
        {
            stopwatch.stop();
            finishedListener.actionPerformed( new ItemActionEvent<Long>( this,
                stopwatch.getElapsed() ) );
        }
    }
}
