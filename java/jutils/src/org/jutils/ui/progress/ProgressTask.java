package org.jutils.ui.progress;

import org.jutils.concurrent.IStoppableTask;
import org.jutils.concurrent.ITaskStopManager;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ProgressTask implements IStoppableTask
{
    /**  */
    private final IProgressView progress;
    /**  */
    private final IProgressTask task;

    /***************************************************************************
     * @param progress
     * @param task
     **************************************************************************/
    public ProgressTask( IProgressView progress, IProgressTask task )
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
