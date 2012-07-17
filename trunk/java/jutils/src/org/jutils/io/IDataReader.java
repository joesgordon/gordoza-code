package org.jutils.io;

import java.io.IOException;

public interface IDataReader<T>
{
    /***************************************************************************
     * @param stream
     * @return
     * @throws IOException
     **************************************************************************/
    public T read( IDataStream stream ) throws IOException;
}
