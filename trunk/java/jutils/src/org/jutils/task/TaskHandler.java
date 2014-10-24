package org.jutils.task;

import org.jutils.concurrent.ITaskStopManager;

public class TaskHandler implements ITaskHandler
{
    private final ITaskView view;
    private final ITaskStopManager stopManager;

    public TaskHandler( ITaskView view, ITaskStopManager stopManager )
    {
        this.view = view;
        this.stopManager = stopManager;
    }

    @Override
    public boolean canContinue()
    {
        return stopManager.continueProcessing();
    }

    @Override
    public void signalMessage( String message )
    {
        view.signalMessage( message );
    }

    @Override
    public void signalPercent( int percent )
    {
        view.signalPercent( percent );
    }

    @Override
    public void signalError( TaskError error )
    {
        view.signalError( error );
    }
}
