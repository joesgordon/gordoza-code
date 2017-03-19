package org.jutils;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jutils.concurrent.TaskStopManager;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class StoppableTest
{
    /***************************************************************************
     * Test method for {@link org.jutils.concurrent.Stoppable#canContinue()}.
     **************************************************************************/
    @Test
    public final static void testCanContinue()
    {
        TaskStopManager stopper = new TaskStopManager();
        boolean canContinuePrior = stopper.continueProcessing();
        stopper.stop();
        boolean canContinuePost = stopper.continueProcessing();
        assertTrue( canContinuePrior && !canContinuePost );
    }

    /***************************************************************************
     * Test method for {@link org.jutils.concurrent.Stoppable#isFinished()}.
     **************************************************************************/
    @Test
    public final static void testIsFinished()
    {
        TaskStopManager stopper = new TaskStopManager();

        boolean isFinishedPrior = stopper.isFinished();
        stopper.signalFinished();
        boolean isFinishedPost = stopper.isFinished();
        assertTrue( !isFinishedPrior && isFinishedPost );
    }

    /***************************************************************************
     * Test method for {@link org.jutils.concurrent.Stoppable#stop()}.
     **************************************************************************/
    @Test
    public final static void testStop()
    {
        TaskStopManager stopper = new TaskStopManager();

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
