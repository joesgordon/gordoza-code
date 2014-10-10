package org.jutils.ui.progress;

import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

/*******************************************************************************
 * An adapter to an {@link IProgressView} from an {@link IProgressView} for
 * which each of the views methods are called on the Event Dispatch Thread.
 ******************************************************************************/
public class EdtProgressViewAdapter implements IProgressView
{
    /** The view that is wrapped by this class. */
    private final IProgressView view;
    /** The last percent set for the view. */
    private int percent;

    /***************************************************************************
     * Creates a new view that wraps the given view.
     * @param view the view to be wrapped.
     **************************************************************************/
    public EdtProgressViewAdapter( IProgressView view )
    {
        this.view = view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addCompleteListener( ActionListener l )
    {
        view.addCompleteListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addCancelListener( ActionListener l )
    {
        view.addCancelListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void signalComplete()
    {
        SwingUtilities.invokeLater( new SignalCompleteRunnable() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void signalError( String title, String message )
    {
        try
        {
            SwingUtilities.invokeAndWait( new SignalErrorRunnable( title,
                message ) );
        }
        catch( InterruptedException e )
        {
        }
        catch( InvocationTargetException e )
        {
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void signalLargeError( String title, String message, String errors )
    {
        try
        {
            SwingUtilities.invokeAndWait( new SignalLargeErrorRunnable( title,
                message, errors ) );
        }
        catch( InterruptedException e )
        {
        }
        catch( InvocationTargetException e )
        {
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void signalException( String title, Throwable ex )
    {
        signalException( title, null, ex );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void signalException( String title, String message, Throwable ex )
    {
        try
        {
            SwingUtilities.invokeAndWait( new SignalExceptionRunnable( title,
                message, ex ) );
        }
        catch( InterruptedException e )
        {
        }
        catch( InvocationTargetException e )
        {
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setPercentComplete( int percent )
    {
        if( this.percent != percent )
        {
            this.percent = percent;
            SwingUtilities.invokeLater( new SetPercentCompleteRunnable( percent ) );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setTitle( String title )
    {
        SwingUtilities.invokeLater( new SetTitleRunnable( title ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setMessage( String message )
    {
        SwingUtilities.invokeLater( new SetMessageRunnable( message ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setIndeterminate( boolean indeterminate )
    {
        SwingUtilities.invokeLater( new SetIndeterminateRunnable( indeterminate ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setVisible( boolean visible )
    {
        SwingUtilities.invokeLater( new SetVisibleRunnable( visible ) );
    }

    /***************************************************************************
     * Runnable to signal complete on the EDT.
     **************************************************************************/
    private class SignalCompleteRunnable implements Runnable
    {
        @Override
        public void run()
        {
            view.signalComplete();
        }
    }

    /***************************************************************************
     * Runnable to signal an error on the EDT.
     **************************************************************************/
    private class SignalErrorRunnable implements Runnable
    {
        private final String title;
        private final String message;

        public SignalErrorRunnable( String title, String message )
        {
            this.title = title;
            this.message = message;
        }

        @Override
        public void run()
        {
            view.signalError( title, message );
        }
    }

    /***************************************************************************
     * Runnable to signal an error on the EDT.
     **************************************************************************/
    private class SignalLargeErrorRunnable implements Runnable
    {
        private final String title;
        private final String message;
        private final String errors;

        public SignalLargeErrorRunnable( String title, String message,
            String errors )
        {
            this.title = title;
            this.message = message;
            this.errors = errors;
        }

        @Override
        public void run()
        {
            view.signalLargeError( title, message, errors );
        }
    }

    /***************************************************************************
     * Runnable to set percent complete on the EDT.
     **************************************************************************/
    private class SetPercentCompleteRunnable implements Runnable
    {
        private final int percent;

        public SetPercentCompleteRunnable( int percent )
        {
            this.percent = percent;
        }

        @Override
        public void run()
        {
            view.setPercentComplete( percent );
        }
    }

    /***************************************************************************
     * Runnable to set the view title on the EDT.
     **************************************************************************/
    private class SetTitleRunnable implements Runnable
    {
        private final String title;

        public SetTitleRunnable( String title )
        {
            this.title = title;
        }

        @Override
        public void run()
        {
            view.setTitle( title );
        }
    }

    /***************************************************************************
     * Runnable to set the current message on the EDT.
     **************************************************************************/
    private class SetMessageRunnable implements Runnable
    {
        private final String message;

        public SetMessageRunnable( String message )
        {
            this.message = message;
        }

        @Override
        public void run()
        {
            view.setMessage( message );
        }
    }

    /***************************************************************************
     * Runnable to signal progress indeterminate on the EDT.
     **************************************************************************/
    private class SetIndeterminateRunnable implements Runnable
    {
        private final boolean indeterminate;

        public SetIndeterminateRunnable( boolean indeterminate )
        {
            this.indeterminate = indeterminate;
        }

        @Override
        public void run()
        {
            view.setIndeterminate( indeterminate );
        }
    }

    /***************************************************************************
     * Runnable to make the view visible or hidden on the EDT.
     **************************************************************************/
    private class SetVisibleRunnable implements Runnable
    {
        private final boolean visible;

        public SetVisibleRunnable( boolean visible )
        {
            this.visible = visible;
        }

        @Override
        public void run()
        {
            view.setVisible( visible );
        }
    }

    private class SignalExceptionRunnable implements Runnable
    {
        private String title;
        private String message;
        private Throwable ex;

        public SignalExceptionRunnable( String title, String message,
            Throwable ex )
        {
            this.title = title;
            this.message = message;
            this.ex = ex;
        }

        @Override
        public void run()
        {
            view.signalException( title, message, ex );
        }
    }
}
