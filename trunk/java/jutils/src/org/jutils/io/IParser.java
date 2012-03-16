package org.jutils.io;

public interface IParser<T>
{
    public T parseItem( String str ) throws IllegalArgumentException;
}
