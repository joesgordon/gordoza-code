package org.jutils.io;

import java.io.IOException;

import org.jutils.ValidationException;

/*******************************************************************************
 * Reads an object of the provided type from a data stream.
 * @param <T> the type of object to be read.
 ******************************************************************************/
public interface IDataReader<T> extends IReader<T, IDataStream>
{
    /***************************************************************************
     * Reads the object from the current position of the provided stream.
     * @param stream the stream containing the object to be read.
     * @return the object read.
     * @throws IOException if any I/O error occurs.
     * @throws ValidationException if the data is improperly formatted or
     * otherwise out of specification.
     **************************************************************************/
    @Override
    public T read( IDataStream stream ) throws IOException, ValidationException;
}
