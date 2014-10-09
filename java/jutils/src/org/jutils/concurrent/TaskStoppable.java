package org.jutils.concurrent;

import org.utils.ui.progress.IProgressView;
import org.utils.ui.progress.ProgressView.IProgressTask;

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
