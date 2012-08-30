package org.jutils.io;

import java.io.IOException;

public interface IDataWriter<T> extends IWriter<T, IDataStream>
{
    /***************************************************************************
     * @param stream
     * @return
     * @throws IOException
     **************************************************************************/
    public void write( T data, IDataStream stream ) throws IOException;
}
