package org.jutils.concurrent;

public interface IProcessor<T>
{
    public void process( T item );
}
