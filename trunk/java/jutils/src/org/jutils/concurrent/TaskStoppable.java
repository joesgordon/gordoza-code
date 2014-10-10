package org.jutils.concurrent;

import org.jutils.ui.progress.IProgressTask;
import org.jutils.ui.progress.IProgressView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TaskStoppable implements IStoppableTask
{
    /**  */
    private final IProgressView progress;
    /**  */
    private final IProgressTask task;

    /***************************************************************************
     * @param progress
     * @param task
     **************************************************************************/
    public TaskStoppable( IProgressView progress, IProgressTask task )
    {
        this.progress = progress;
        this.task = task;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void run( ITaskStopManager stopper )
    {
        task.run( stopper, progress );
    }
}
