package org.jutils;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jutils.concurrent.Stopper;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class StoppableTest
{
    /***************************************************************************
     * Test method for {@link org.jutils.concurrent.Stoppable#canContinue()}.
     **************************************************************************/
    @Test
    public final void testCanContinue()
    {
        Stopper stopper = new Stopper();
        boolean canContinuePrior = stopper.continueProcessing();
        stopper.stop();
        boolean canContinuePost = stopper.continueProcessing();
        assertTrue( canContinuePrior && !canContinuePost );
    }

    /***************************************************************************
     * Test method for {@link org.jutils.concurrent.Stoppable#isFinished()}.
     **************************************************************************/
    @Test
    public final void testIsFinished()
    {
        Stopper stopper = new Stopper();

        boolean isFinishedPrior = stopper.isFinished();
        stopper.signalFinished();
        boolean isFinishedPost = stopper.isFinished();
        assertTrue( !isFinishedPrior && isFinishedPost );
    }

    /***************************************************************************
     * Test method for {@link org.jutils.concurrent.Stoppable#stop()}.
     **************************************************************************/
    @Test
    public final void testStop()
    {
        Stopper stopper = new Stopper();

        stopper.stop();
        stopper.signalFinished();

        try
        {
            stopper.waitFor();
        }
        catch( InterruptedException e )
        {
        }

        assertTrue( stopper.isFinished() );
    }
}
