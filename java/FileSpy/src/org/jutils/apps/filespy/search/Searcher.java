package org.jutils.apps.filespy.search;

import org.jutils.apps.filespy.data.SearchParams;
import org.jutils.apps.filespy.ui.ResultsView;
import org.jutils.concurrent.Stoppable;
import org.jutils.ui.StatusBarPanel;
import org.jutils.ui.event.ItemActionEvent;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.utils.Stopwatch;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Searcher
{
    /**  */
    private final ResultsView resultsView;
    /**  */
    private final StatusBarPanel statusBar;

    /**  */
    private Stoppable stoppableTask;
    /**  */
    private Thread searchThread;

    /***************************************************************************
     * @param resultsView
     * @param fileSpyFrameView
     * @param statusBar
     **************************************************************************/
    public Searcher( ResultsView resultsView, StatusBarPanel statusBar )
    {
        this.resultsView = resultsView;
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
        SearchResultsHandler handler = new SearchResultsHandler( resultsView,
            statusBar );

        statusBar.setText( "" );
        resultsView.clearPanel();

        SearchTask searchTask = new SearchTask( handler, params, finalizer );

        stoppableTask = new Stoppable( searchTask );
        searchThread = new Thread( stoppableTask, "FileSpy Search Thread" );
        searchThread.start();
    }

    /***************************************************************************
     * 
     **************************************************************************/
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
            finishedListener.actionPerformed(
                new ItemActionEvent<Long>( this, stopwatch.getElapsed() ) );
        }
    }
}
