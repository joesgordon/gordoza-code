package org.jutils.io;

import org.jutils.ValidationException;

public interface IParser<T>
{
    public T parse( String str ) throws ValidationException;
}
