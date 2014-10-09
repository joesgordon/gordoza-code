package org.utils.ui.progress;

import java.awt.event.ActionListener;

import org.jutils.ui.event.ActionListenerList;

/*******************************************************************************
 * Defines an {@link IProgressView} for the command line user interface.
 ******************************************************************************/
public class CommandLineProgressView implements IProgressView
{
    /** List of complete listeners called upon {@link #signalComplete()} */
    private final ActionListenerList completeListeners;

    /***************************************************************************
     * Creates a new command line progress view.
     **************************************************************************/
    public CommandLineProgressView()
    {
        this.completeListeners = new ActionListenerList();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addCancelListener( ActionListener l )
    {
        // TODO Support cancel.
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addCompleteListener( ActionListener l )
    {
        completeListeners.addListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void signalComplete()
    {
        completeListeners.fireListeners( this, 0, null );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void signalError( String title, String message )
    {
        System.err.println( title + " - " + message );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void signalLargeError( String title, String message, String errors )
    {
        signalError( title, message );
        System.err.println( errors );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setPercentComplete( int percent )
    {
        // Ignore percent complete.
        System.out.println( percent + "%" );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setTitle( String title )
    {
        System.out.println( title );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setMessage( String message )
    {
        System.out.println( "\t" + message );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setIndeterminate( boolean indeterminate )
    {
        ;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setVisible( boolean visible )
    {
        ;
    }

    @Override
    public void signalException( String title, Throwable ex )
    {
        signalException( title, null, ex );
    }

    @Override
    public void signalException( String title, String message, Throwable ex )
    {
        System.out.print( title );
        if( message != null )
        {
            System.out.println( ": " + message );
        }
        ex.printStackTrace();
    }
}
