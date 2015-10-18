package org.jutils.task;

import java.awt.event.ActionListener;

import org.jutils.Stopwatch;
import org.jutils.ui.event.ActionListenerList;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MultiTaskRunner implements Runnable
{
    /**  */
    final MultiTaskHandler handler;
    /**  */
    private final TaskPool pool;

    /**  */
    private final ActionListenerList finishedListeners;

    private TaskMetrics metrics;

    /***************************************************************************
     * @param tasker
     * @param view
     * @param numThreads
     **************************************************************************/
    public MultiTaskRunner( IMultiTask tasker, IMultiTaskView view,
        int numThreads )
    {
        this.handler = new MultiTaskHandler( tasker, view );
        this.pool = new TaskPool( handler, numThreads );
        this.finishedListeners = new ActionListenerList();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void run()
    {
        long start;
        long stop;
        Stopwatch watch = new Stopwatch();

        start = watch.start();
        pool.start();
        stop = watch.stop();

        metrics = new TaskMetrics( start, stop, !handler.canContinue() );

        finishedListeners.fireListeners( this, 0, null );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void stop()
    {
        pool.shutdown();
    }

    public TaskMetrics getMetrics()
    {
        return metrics;
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addFinishedListener( ActionListener l )
    {
        finishedListeners.addListener( l );
    }
}
