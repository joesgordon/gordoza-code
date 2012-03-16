package org.jutils.io;

import java.io.*;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public interface ISerializer<T>
{
    /***************************************************************************
     * @param stream
     * @return
     * @throws IOException
     **************************************************************************/
    public T read( InputStream stream ) throws IOException;

    /***************************************************************************
     * @param t
     * @param stream
     * @throws IOException
     **************************************************************************/
    public void write( T item, OutputStream stream ) throws IOException;
}
