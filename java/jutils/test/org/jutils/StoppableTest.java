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
    public void testCanContinue()
    {
        TaskStopManager stopper = new TaskStopManager();
        boolean canContinuePrior = stopper.canContinue();
        stopper.stop();
        boolean canContinuePost = stopper.canContinue();
        assertTrue( canContinuePrior && !canContinuePost );
    }

    /***************************************************************************
     * Test method for {@link org.jutils.concurrent.Stoppable#isFinished()}.
     **************************************************************************/
    @Test
    public void testIsFinished()
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
    public void testStop()
    {
        TaskStopManager stopper = new TaskStopManager();

        stopper.stop();
        stopper.signalFinished();

        stopper.waitFor();

        assertTrue( stopper.isFinished() );
    }
}
