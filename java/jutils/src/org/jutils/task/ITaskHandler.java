package org.jutils.task;

public interface ITaskHandler
{
    public boolean canContinue();

    public void signalMessage( String message );

    public void signalPercent( int percent );

    public void signalError( TaskError error );
}
