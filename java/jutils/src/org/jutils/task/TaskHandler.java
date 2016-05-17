package org.jutils.task;

import org.jutils.concurrent.ITaskStopManager;
import org.jutils.ui.event.ItemActionListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TaskHandler implements ITaskHandler
{
    /**  */
    private final ITaskView view;
    /**  */
    final ITaskStopManager stopManager;

    /***************************************************************************
     * @param view
     * @param stopManager
     **************************************************************************/
    public TaskHandler( ITaskView view, ITaskStopManager stopManager )
    {
        this.view = view;
        this.stopManager = stopManager;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean canContinue()
    {
        return stopManager.continueProcessing();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void signalMessage( String message )
    {
        view.signalMessage( message );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean signalPercent( int percent )
    {
        return view.signalPercent( percent );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void signalError( TaskError error )
    {
        view.signalError( error );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void signalFinished()
    {
        stopManager.signalFinished();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void stop()
    {
        stopManager.stop();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void stopAndWait() throws InterruptedException
    {
        stopManager.stopAndWait();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addFinishedListener( ItemActionListener<Boolean> l )
    {
        stopManager.addFinishedListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void removeFinishedListener( ItemActionListener<Boolean> l )
    {
        stopManager.removeFinishedListener( l );
    }
}
