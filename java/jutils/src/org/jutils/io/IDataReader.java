package org.jutils.io;

import java.io.IOException;

import org.jutils.ValidationException;

public interface IDataReader<T> extends IReader<T, IDataStream>
{
    /***************************************************************************
     * @param stream
     * @return
     * @throws IOException
     **************************************************************************/
    public T read( IDataStream stream ) throws IOException, ValidationException;
}
