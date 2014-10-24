package org.jutils.task;

import org.jutils.Stopwatch;
import org.jutils.concurrent.TaskStopManager;

public class TaskRunner
{
    private final ITask task;
    private final ITaskView view;

    public TaskRunner( ITask task, ITaskView view )
    {
        this.task = task;
        this.view = view;
    }

    public TaskMetrics run()
    {
        Stopwatch watch = new Stopwatch();
        TaskStopManager stopManager = new TaskStopManager();
        TaskHandler handler = new TaskHandler( view, stopManager );

        long start = watch.start();

        task.run( handler );

        long stop = watch.stop();

        return new TaskMetrics( start, stop );
    }
}
