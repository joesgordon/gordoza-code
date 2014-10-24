package org.jutils.task;

public interface ITaskHandler
{
    public boolean canContinue();

    public void signalMessage( String message );

    public void signalPercentComplete( int percent );

    public void signalError( TaskError error );
}
