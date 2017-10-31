package org.jutils.net;

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
    public final String address;
    /**  */
    public final int port;
    /**  */
    public final byte [] contents;

    /***************************************************************************
     * 
     **************************************************************************/
    public NetMessage( boolean received, String address, int port,
        byte [] contents )
    {
        this( received, LocalDateTime.now(), address, port, contents );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public NetMessage( boolean received, LocalDateTime time, String address,
        int port, byte [] contents )
    {
        this.received = received;
        this.time = time;
        this.address = address;
        this.port = port;
        this.contents = contents;
    }
}
