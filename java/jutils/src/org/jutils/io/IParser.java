package org.jutils.io;

import org.jutils.ValidationException;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public interface IParser<T>
{
    /***************************************************************************
     * @param str
     * @return
     * @throws ValidationException
     **************************************************************************/
    public T parse( String str ) throws ValidationException;
}
