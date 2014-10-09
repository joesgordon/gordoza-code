package org.utils.ui.progress;

import java.awt.event.ActionListener;

// TODO add method of prompting for an answer.

/*******************************************************************************
 * Defines set of methods for updating the view with a task's progress.
 ******************************************************************************/
public interface IProgressView
{
    /***************************************************************************
     * Add a listener to be called upon cancellation of the task.
     * @param l the listener to be called.
     **************************************************************************/
    public void addCancelListener( ActionListener l );

    /***************************************************************************
     * Adds a listener to be called upon completion of the task.
     * @param l the listener to be called.
     **************************************************************************/
    public void addCompleteListener( ActionListener l );

    /***************************************************************************
     * Signals to all listeners that this task is complete. By contract, do not
     * expect the implementation to necessarily be synchronous or asynchronous.
     **************************************************************************/
    public void signalComplete();

    /***************************************************************************
     * Signals to the user that an error has occurred.
     * @param title the title of the error.
     * @param message the error message.
     **************************************************************************/
    public void signalError( String title, String message );

    /***************************************************************************
     * @param title
     * @param message
     * @param errors
     **************************************************************************/
    public void signalLargeError( String title, String message, String errors );

    /***************************************************************************
     * Signals to the user that the provided exception has been thrown.
     * @param title the title of the error.
     * @param ex the exception thrown.
     **************************************************************************/
    public void signalException( String title, Throwable ex );

    /***************************************************************************
     * Signals to the user that the provided exception has been thrown.
     * @param title the title of the error.
     * @param message a custom message to be shown.
     * @param ex the exception thrown.
     **************************************************************************/
    public void signalException( String title, String message, Throwable ex );

    /***************************************************************************
     * Sets the percent complete for the view.
     * @param the percent value between 0 and 100 inclusive that the task is
     * complete.
     **************************************************************************/
    public void setPercentComplete( int percent );

    /***************************************************************************
     * Sets the title for this view.
     * @param title the title for this view.
     **************************************************************************/
    public void setTitle( String title );

    /***************************************************************************
     * Sets the current message for this view.
     * @param message the current message for this view.
     **************************************************************************/
    public void setMessage( String message );

    /***************************************************************************
     * Sets the views progress as indeterminate or not. When indeterminate,
     * updates to the task's progress will be ignored.
     * @param indeterminate indeterminate if {@code true}, determinate
     * otherwise.
     **************************************************************************/
    public void setIndeterminate( boolean indeterminate );

    /***************************************************************************
     * Makes the view visible or not.
     * @param visible visible if {@code true}, hidden otherwise.
     **************************************************************************/
    public void setVisible( boolean visible );
}
