package org.jutils.concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.jutils.ui.event.EventListenerList;
import org.jutils.ui.event.IEventListener;

public final class Stopper implements IStopper
{
    /** Execution continues as long as {@code continueRunning} is {@code true}. */
    private volatile boolean continueRunning;
    /** {@code true} after {@link #run()} returns {@code false} otherwise. */
    private volatile boolean isFinished;
    /** Lock used to protect the finished flag and the stop condition. */
    private ReentrantLock stopLock;
    /** Condition used to signal that {@link #run()} is complete. */
    private Condition stopCondition;
    /**  */
    private EventListenerList stoppedListeners;

    public Stopper()
    {
        continueRunning = true;
        isFinished = false;
        stopLock = new ReentrantLock();
        stopCondition = stopLock.newCondition();
        stoppedListeners = new EventListenerList();
    }

    @Override
    public void stop()
    {
        continueRunning = false;
    }

    @Override
    public boolean isFinished()
    {
        return isFinished;
    }

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

    @Override
    public void stopAndWaitFor() throws InterruptedException
    {
        stop();
        waitFor();
    }

    @Override
    public boolean continueProcessing()
    {
        return continueRunning;
    }

    @Override
    public void signalFinished()
    {
        stopLock.lock();
        isFinished = true;
        stopCondition.signalAll();
        stopLock.unlock();
    }

    @Override
    public void addStoppedListener( IEventListener l )
    {
        stoppedListeners.addListener( l );
    }
}
