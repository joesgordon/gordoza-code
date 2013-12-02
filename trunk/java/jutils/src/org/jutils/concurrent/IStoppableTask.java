package org.jutils.concurrent;

/*******************************************************************************
 * Represents a task that has the ability to be stopped.
 ******************************************************************************/
public interface IStoppableTask
{
    /***************************************************************************
     * Runs the task periodically checking the provided stop manager.
     * @param stopManager the object that contains the execution state for this
     * task.
     **************************************************************************/
    public void run( ITaskStopManager stopManager );
}
