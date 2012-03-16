package org.jutils.io;

import java.io.IOException;
import java.io.Reader;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public interface IReader<T>
{
    /***************************************************************************
     * @return
     * @throws IOException
     **************************************************************************/
    public T read() throws IOException;

    /***************************************************************************
     * @return
     **************************************************************************/
    public Reader getStream();
}
