package org.jutils.concurrent;

import org.jutils.ui.event.ItemActionListener;

/*******************************************************************************
 * {@link Runnable} that may be stopped synchronously or asynchronously.
 ******************************************************************************/
public class Stoppable implements Runnable
{
    /** Object used to hold the continue/stop state. */
    private final Stopper stopper;
    /** The task to run */
    private final IStoppable task;

    /***************************************************************************
     * Creates the {@link Runnable}
     **************************************************************************/
    public Stoppable( IStoppable task )
    {
        this( task, new Stopper() );
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
    public Stoppable( IStoppable task, Stopper stopper )
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
     * @see Stopper#stop()
     **************************************************************************/
    public void stop()
    {
        stopper.stop();
    }

    /***************************************************************************
     * @see Stopper#isFinished()
     **************************************************************************/
    public boolean isFinished()
    {
        return stopper.isFinished();
    }

    /***************************************************************************
     * @see Stopper#waitFor()
     **************************************************************************/
    public void waitFor() throws InterruptedException
    {
        stopper.waitFor();
    }

    /***************************************************************************
     * @see Stopper#stopAndWaitFor()
     **************************************************************************/
    public void stopAndWaitFor() throws InterruptedException
    {
        stopper.stopAndWaitFor();
    }
}
