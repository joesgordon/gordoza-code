package org.jutils;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jutils.concurrent.TaskHandler;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class StoppableTest
{
    /***************************************************************************
     * Test method for {@link org.jutils.concurrent.Taskable#canContinue()}.
     **************************************************************************/
    @Test
    public void testCanContinue()
    {
        TaskHandler stopper = new TaskHandler();
        boolean canContinuePrior = stopper.canContinue();
        stopper.stop();
        boolean canContinuePost = stopper.canContinue();
        assertTrue( canContinuePrior && !canContinuePost );
    }

    /***************************************************************************
     * Test method for {@link org.jutils.concurrent.Taskable#isFinished()}.
     **************************************************************************/
    @Test
    public void testIsFinished()
    {
        TaskHandler stopper = new TaskHandler();

        boolean isFinishedPrior = stopper.isFinished();
        stopper.signalFinished();
        boolean isFinishedPost = stopper.isFinished();
        assertTrue( !isFinishedPrior && isFinishedPost );
    }

    /***************************************************************************
     * Test method for {@link org.jutils.concurrent.Taskable#stop()}.
     **************************************************************************/
    @Test
    public void testStop()
    {
        TaskHandler stopper = new TaskHandler();

        stopper.stop();
        stopper.signalFinished();

        stopper.waitFor();

        assertTrue( stopper.isFinished() );
    }
}
