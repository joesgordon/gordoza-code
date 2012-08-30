package org.jutils.io;

import java.io.IOException;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public interface IWriter<T, R>
{
    /***************************************************************************
     * @param item
     * @return
     * @throws IOException
     **************************************************************************/
    public void write( T item, R resource ) throws IOException;
}
