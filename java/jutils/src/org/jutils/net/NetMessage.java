package org.jutils.net;

import java.net.InetAddress;
import java.time.LocalDateTime;

/*******************************************************************************
 * 
 ******************************************************************************/
public class NetMessage
{
    /**  */
    public final boolean received;
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
    public NetMessage( boolean received, InetAddress address, int port,
        byte [] contents )
    {
        this.received = received;
        this.time = LocalDateTime.now();
        this.address = address;
        this.port = port;
        this.contents = contents;
    }
}
