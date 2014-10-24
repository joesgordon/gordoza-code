package org.jutils.task;

public class MultiTaskRunner implements Runnable
{
    private final MultiTaskHandler handler;
    private final TaskPool pool;

    public MultiTaskRunner( ITasker tasker, IMultiTaskView view, int numThreads )
    {
        this.handler = new MultiTaskHandler( tasker, view );
        this.pool = new TaskPool( handler, numThreads );
    }

    @Override
    public void run()
    {
        pool.start();
    }
}
