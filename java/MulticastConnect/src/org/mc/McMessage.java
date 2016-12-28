package org.mc;

import org.jutils.time.TimeUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class McMessage
{
    /**  */
    public final String address;
    /**  */
    public final int port;
    /** Time in millis */
    public final long time;
    /**  */
    public final byte[] contents;

    /***************************************************************************
     * 
     **************************************************************************/
    public McMessage( String address, int port, byte[] contents )
    {
        this( address, port, TimeUtils.now(), contents );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public McMessage( String address, int port, long time, byte[] contents )
    {
        this.address = address;
        this.port = port;
        this.time = time;
        this.contents = contents;
    }
}
