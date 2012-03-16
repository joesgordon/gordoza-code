package org.jutils.concurrent;


/*******************************************************************************
 * {@link Runnable} that may be stopped synchronously or asynchronously.
 ******************************************************************************/
public class Stoppable implements Runnable
{
    /**  */
    private final Stopper stopper;
    /**  */
    private final IStoppable stoppable;

    /***************************************************************************
     * Creates the {@link Runnable}
     **************************************************************************/
    public Stoppable( IStoppable stoppable )
    {
        this.stopper = new Stopper();
        this.stoppable = stoppable;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void run()
    {
        stoppable.run( stopper );
        stopper.signalFinished();
    }

    public void stop()
    {
        stopper.stop();
    }

    public boolean isFinished()
    {
        return stopper.isFinished();
    }

    public void waitFor() throws InterruptedException
    {
        stopper.waitFor();
    }

    public void stopAndWaitFor() throws InterruptedException
    {
        stopper.stopAndWaitFor();
    }

    public boolean continueProcessing()
    {
        return stopper.continueProcessing();
    }
}
