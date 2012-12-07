package org.jutils.io;

import java.io.IOException;

/*******************************************************************************
 * Defines a generic method of reading a type from a resource.
 * @param <T> the type to be read.
 * @param <R> the resource from which the type is read.
 ******************************************************************************/
public interface IReader<T, R>
{
    /***************************************************************************
     * Reads the type from the provided resource.
     * @return the type read from the provided resource.
     * @throws IOException any I/O error that occurs.
     * @throws RuntimeFormatException any error due to incorrect format or other
     * non-I/O errors that might arise
     **************************************************************************/
    public T read( R resource ) throws IOException, RuntimeFormatException;
}
