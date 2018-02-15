package org.jutils.task;

import org.jutils.ui.event.ItemActionListener;

/*******************************************************************************
 * Defines a set of common functions an {@link ITask} needs to report status and
 * determine if it should stop.
 ******************************************************************************/
public interface ITaskHandler
{
    /***************************************************************************
     * Issues the provided status message to any listeners. Intended for
     * messages such as "Loading file 1 of 34: config.xml"
     * @param message the status message from the task.
     **************************************************************************/
    public void signalMessage( String message );

    /***************************************************************************
     * Issues the provided percent complete to any listeners if the provided
     * percent is different from the previous percent. A percent of -1 indicates
     * an indeterminate percent.
     * @param percent the current percent complete of the task, range (-1, 100).
     * @return {@code true} if the percent complete changed enough to issue the
     * update; {@code false} otherwise.
     **************************************************************************/
    public boolean signalPercent( int percent );

    /***************************************************************************
     * Issues the provided error to any listeners. This will normally result in
     * the task stopping prematurely.
     * @param error the error from the task.
     * @see #signalFinished()
     **************************************************************************/
    public void signalError( TaskError error );

    /***************************************************************************
     * Invoked by the task when it has finished processing and is about to
     * return.
     **************************************************************************/
    public void signalFinished();

    /***************************************************************************
     * Called by the task to see if it should continue processing or if an
     * external entity has requested that it cease.
     * @return {@code true} if the task can continue, {@code false} otherwise.
     **************************************************************************/
    public boolean canContinue();

    /***************************************************************************
     * Stops the task being handled. Indicates to the handler that an external
     * entity requests the thread cease processing.
     **************************************************************************/
    public void stop();

    /***************************************************************************
     * Stops the task being handled and waits for {@link #signalFinished()} to
     * be invoked.
     * @throws InterruptedException if the thread waiting on the task to finish
     * is interrupted. In most cases, this can be ignored.
     **************************************************************************/
    public void stopAndWait() throws InterruptedException;

    /***************************************************************************
     * Adds the provided listener to a list that is notified when the task
     * completes. The boolean contained in the event is {@code true} when the
     * task completes normally and {@code false} when it has been interrupted or
     * encounters an error severe enough to cease further processing.
     * @param l the listener to be called when the task completes.
     **************************************************************************/
    public void addFinishedListener( ItemActionListener<Boolean> l );

    /***************************************************************************
     * Removes any listener added by
     * {@link #addFinishedListener(ItemActionListener)}.
     * @param l the listener to be removed.
     **************************************************************************/
    public void removeFinishedListener( ItemActionListener<Boolean> l );
}
