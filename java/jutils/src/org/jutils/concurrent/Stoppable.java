package org.jutils.concurrent;

import org.jutils.ui.event.ItemActionListener;

/*******************************************************************************
 * {@link Runnable} that may be stopped synchronously or asynchronously.
 ******************************************************************************/
public class Stoppable implements Runnable
{
    /** Object used to hold the continue/stop state. */
    private final TaskStopManager stopper;
    /** The task to run */
    private final IStoppableTask task;

    /***************************************************************************
     * Creates the {@link Runnable}
     **************************************************************************/
    public Stoppable( IStoppableTask task )
    {
        this( task, new TaskStopManager() );
    }

    /***************************************************************************
     * Adds a listener to be called when the task completes; reports
     * {@code true} if the process was not stopped preemptively, {@link false}
     * otherwise.
     * @param l the listener to be added.
     **************************************************************************/
    public void addFinishedListener( ItemActionListener<Boolean> l )
    {
        stopper.addFinishedListener( l );
    }

    /***************************************************************************
     * Removes the supplied listener from the list of finished listeners.
     * @param l the listener to be removed.
     **************************************************************************/
    public void removeFinishedListener( ItemActionListener<Boolean> l )
    {
        stopper.removeFinishedListener( l );
    }

    /***************************************************************************
     * Creates the {@link Runnable}
     **************************************************************************/
    public Stoppable( IStoppableTask task, TaskStopManager stopper )
    {
        this.stopper = stopper;
        this.task = task;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void run()
    {
        task.run( stopper );
        stopper.signalFinished();
    }

    /***************************************************************************
     * @see TaskStopManager#stop()
     **************************************************************************/
    public void stop()
    {
        stopper.stop();
    }

    /***************************************************************************
     * @see TaskStopManager#isFinished()
     **************************************************************************/
    public boolean isFinished()
    {
        return stopper.isFinished();
    }

    /***************************************************************************
     * @see TaskStopManager#waitFor()
     **************************************************************************/
    public void waitFor() throws InterruptedException
    {
        stopper.waitFor();
    }

    /***************************************************************************
     * @see TaskStopManager#stopAndWaitFor()
     **************************************************************************/
    public void stopAndWaitFor() throws InterruptedException
    {
        stopper.stopAndWaitFor();
    }
}
