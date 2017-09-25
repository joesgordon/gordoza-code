package org.jutils.net;

import java.net.InetAddress;
import java.time.LocalDateTime;

/*******************************************************************************
 * 
 ******************************************************************************/
public class NetMessage
{
    /** Time of transmission or reception. */
    public final LocalDateTime time;
    /**  */
    public final InetAddress address;
    /**  */
    public final int port;
    /**  */
    public final byte [] contents;

    /***************************************************************************
     * 
     **************************************************************************/
    public NetMessage( byte [] contents, InetAddress address, int port )
    {
        this.time = LocalDateTime.now();
        this.address = address;
        this.port = port;
        this.contents = contents;
    }
}
