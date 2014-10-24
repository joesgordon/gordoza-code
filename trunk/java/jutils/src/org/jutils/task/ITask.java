package org.jutils.task;

public interface ITask
{
    public void run( ITaskHandler handler );

    public String getName();
}
