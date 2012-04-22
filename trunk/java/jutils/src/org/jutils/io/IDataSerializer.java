package org.jutils.io;

import java.io.IOException;

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
    public T read( IDataStream stream ) throws IOException;

    /***************************************************************************
     * @param t
     * @param stream
     * @throws IOException
     **************************************************************************/
    public void write( T item, IDataStream stream ) throws IOException;
}
