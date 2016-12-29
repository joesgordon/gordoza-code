package org.mc.io;

import java.io.Closeable;
import java.io.IOException;

import org.mc.McMessage;

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
    public McMessage txMessage( byte[] buf ) throws IOException;

    /***************************************************************************
     * @return
     * @throws IOException
     **************************************************************************/
    public McMessage rxMessage() throws IOException;
}
