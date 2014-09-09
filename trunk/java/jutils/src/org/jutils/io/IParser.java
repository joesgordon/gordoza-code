package org.jutils.io;

import org.jutils.ui.validation.ValidationException;

public interface IParser<T>
{
    public T parse( String str ) throws ValidationException;
}
