package org.jutils.task;

import java.awt.Component;

import org.jutils.ui.event.ItemActionListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class NullTaskHandler implements ITaskHandler
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean canContinue()
    {
        return true;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void signalMessage( String message )
    {
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void signalPercent( int percent )
    {
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void signalError( TaskError error )
    {
        throw new RuntimeException( error.message );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void signalFinished()
    {
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void stop()
    {
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void stopAndWait() throws InterruptedException
    {
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addFinishedListener( ItemActionListener<Boolean> l )
    {
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void removeFinishedListener( ItemActionListener<Boolean> l )
    {
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Component getView()
    {
        return null;
    }
}
