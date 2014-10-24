package org.jutils.task;

import java.awt.event.ActionListener;

import org.jutils.ui.event.ActionListenerList;

public class MultiTaskRunner implements Runnable
{
    private final MultiTaskHandler handler;
    private final TaskPool pool;

    private final ActionListenerList finishedListeners;

    public MultiTaskRunner( ITasker tasker, IMultiTaskView view, int numThreads )
    {
        this.handler = new MultiTaskHandler( tasker, view );
        this.pool = new TaskPool( handler, numThreads );
        this.finishedListeners = new ActionListenerList();
    }

    @Override
    public void run()
    {
        pool.start();
        finishedListeners.fireListeners( this, 0, null );
    }

    public void stop()
    {
        pool.shutdown();
    }

    public void addFinishedListener( ActionListener l )
    {
        finishedListeners.addListener( l );
    }
}
