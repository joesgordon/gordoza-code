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

    /***************************************************************************
     * 
     **************************************************************************/
    public void stop()
    {
        stopper.stop();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isFinished()
    {
        return stopper.isFinished();
    }

    /***************************************************************************
     * @throws InterruptedException
     **************************************************************************/
    public void waitFor() throws InterruptedException
    {
        stopper.waitFor();
    }

    /***************************************************************************
     * @throws InterruptedException
     **************************************************************************/
    public void stopAndWaitFor() throws InterruptedException
    {
        stopper.stopAndWaitFor();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean continueProcessing()
    {
        return stopper.continueProcessing();
    }
}
