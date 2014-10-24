package org.jutils.task;

import org.jutils.Stopwatch;
import org.jutils.concurrent.ITaskStopManager;
import org.jutils.concurrent.TaskStopManager;
import org.jutils.ui.event.ItemActionListener;

public class TaskRunner implements Runnable
{
    final ITask task;
    private final ITaskView view;
    private final ITaskStopManager stopManager;

    private TaskMetrics metrics;

    public TaskRunner( ITask task, ITaskView view )
    {
        this( task, view, new TaskStopManager() );
    }

    public TaskRunner( ITask task, ITaskView view, ITaskStopManager stopManager )
    {
        this.task = task;
        this.view = view;
        this.stopManager = stopManager;
    }

    @Override
    public void run()
    {
        Stopwatch watch = new Stopwatch();
        TaskHandler handler = new TaskHandler( view, stopManager );

        long start = watch.start();

        try
        {
            task.run( handler );
        }
        finally
        {
            long stop = watch.stop();

            metrics = new TaskMetrics( start, stop );

            stopManager.signalFinished();
        }
    }

    public TaskMetrics getMetrics()
    {
        return metrics;
    }

    public void stop()
    {
        stopManager.stop();
    }

    public void stopAndWait()
    {
        stopManager.stop();
    }

    public void addFinishedListener( ItemActionListener<Boolean> l )
    {
        stopManager.addFinishedListener( l );
    }
}
