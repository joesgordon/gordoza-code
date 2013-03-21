package org.jutils.concurrent;

// TODO rename to IStoppableTask

public interface IStoppable
{
    public void run( IStopper stopper );
}
