package org.jutils.io;

public interface IParser<T>
{
    public T parse( String str ) throws RuntimeFormatException;
}
