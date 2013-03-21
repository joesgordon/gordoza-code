package org.jutils.concurrent;

// TODO Rename to ITaskStopManager

/*******************************************************************************
 * Represents the execution state (executing or stopped) and the methods to
 * modify said state.
 ******************************************************************************/
public interface IStopper
{
    /***************************************************************************
     * Asynchronously requests that the task stop. Returns immediately.
     **************************************************************************/
    public void stop();

    /***************************************************************************
     * Returns {@code true} if the task is finished, {@code false} otherwise.
     **************************************************************************/
    public boolean isFinished();

    /***************************************************************************
     * Waits for the task to complete. The task is complete when
     * {@link #signalFinished()} is called.
     * @throws InterruptedException if the current thread is interrupted while
     * waiting on the task.
     **************************************************************************/
    public void waitFor() throws InterruptedException;

    /***************************************************************************
     * Calls {@link #stop()} and then {@link #waitFor()}.
     * @throws InterruptedException if the current thread is interrupted while
     * waiting on the task.
     **************************************************************************/
    public void stopAndWaitFor() throws InterruptedException;

    /***************************************************************************
     * Returns {@code true} if stop has not been requested, {@code false}
     * otherwise.
     **************************************************************************/
    public boolean continueProcessing();

    /***************************************************************************
     * Tells this class that the task is finished and therefore all waiting
     * object may be released.
     **************************************************************************/
    public void signalFinished();
}
