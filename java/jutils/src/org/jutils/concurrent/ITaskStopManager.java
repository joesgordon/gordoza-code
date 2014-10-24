package org.jutils.concurrent;

import org.jutils.ui.event.ItemActionListener;

/*******************************************************************************
 * Represents the execution state (executing or stopped) and the methods to
 * modify said state.
 ******************************************************************************/
public interface ITaskStopManager
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
    public void stopAndWait() throws InterruptedException;

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

    /***************************************************************************
     * Adds a listener to be called when {@link #signalFinished()} is called;
     * reports {@code true} if the process was not stopped preemptively, {@link
     * false} otherwise.
     * @param l the listener to be added.
     **************************************************************************/
    public void addFinishedListener( ItemActionListener<Boolean> l );

    /***************************************************************************
     * Removes the supplied listener from the list of finished listeners.
     * @param l the listener to be removed.
     **************************************************************************/
    public void removeFinishedListener( ItemActionListener<Boolean> l );
}
