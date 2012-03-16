package org.jutils.io;

import java.io.*;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public interface IDataSerializer<T>
{
    /***************************************************************************
     * @param stream
     * @return
     * @throws IOException
     **************************************************************************/
    public T read( DataInput stream ) throws IOException;

    /***************************************************************************
     * @param t
     * @param stream
     * @throws IOException
     **************************************************************************/
    public void write( T item, DataOutput stream ) throws IOException;
}
