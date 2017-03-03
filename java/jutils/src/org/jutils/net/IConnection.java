package org.jutils.net;

import java.io.Closeable;
import java.io.IOException;

/*******************************************************************************
 * 
 ******************************************************************************/
public interface IConnection extends Closeable
{
    /***************************************************************************
     * @param buf
     * @return
     * @throws IOException
     **************************************************************************/
    public NetMessage txMessage( byte [] buf ) throws IOException;

    /***************************************************************************
     * @return
     * @throws IOException
     **************************************************************************/
    public NetMessage rxMessage() throws IOException;
}
