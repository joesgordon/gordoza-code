package org.jutils.concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.jutils.ui.event.ItemActionList;
import org.jutils.ui.event.ItemActionListener;

/*******************************************************************************
 * This class is a sort of semaphore that represents the execution state
 * (executing or stopped) and the methods to modify said state.
 ******************************************************************************/
public final class TaskStopManager implements ITaskStopManager
{
    /** Execution continues as long as {@code continueRunning} is {@code true}. */
    private volatile boolean continueRunning;
    /** {@code true} after {@link #run()} returns {@code false} otherwise. */
    private volatile boolean isFinished;
    /** Lock used to protect the finished flag and the stop condition. */
    private final ReentrantLock stopLock;
    /** Condition used to signal that {@link #run()} is complete. */
    private final Condition stopCondition;
    /** List of listeners to be called when {@link #signalFinished()} is called. */
    private final ItemActionList<Boolean> finishedListeners;

    /***************************************************************************
     * Creates a new object.
     **************************************************************************/
    public TaskStopManager()
    {
        this.continueRunning = true;
        this.isFinished = false;
        this.stopLock = new ReentrantLock();
        this.stopCondition = stopLock.newCondition();
        this.finishedListeners = new ItemActionList<Boolean>();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addFinishedListener( ItemActionListener<Boolean> l )
    {
        finishedListeners.addListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void removeFinishedListener( ItemActionListener<Boolean> l )
    {
        finishedListeners.removeListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void stop()
    {
        continueRunning = false;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean isFinished()
    {
        return isFinished;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void waitFor() throws InterruptedException
    {
        stopLock.lock();

        try
        {
            while( !isFinished )
            {
                stopCondition.await();
            }
        }
        finally
        {
            stopLock.unlock();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void stopAndWait() throws InterruptedException
    {
        stop();
        waitFor();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean continueProcessing()
    {
        return continueRunning;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void signalFinished()
    {
        stopLock.lock();
        isFinished = true;
        stopCondition.signalAll();
        stopLock.unlock();

        finishedListeners.fireListeners( this, continueRunning );

        finishedListeners.removeAll();
    }
}
