package org.jutils.net;

import java.time.LocalDateTime;

/*******************************************************************************
 * 
 ******************************************************************************/
public class NetMessage
{
    /** Time of transmission or reception. */
    public final LocalDateTime time;
    /**  */
    public final byte[] contents;

    /***************************************************************************
     * 
     **************************************************************************/
    public NetMessage( byte[] contents )
    {
        this.time = LocalDateTime.now();
        this.contents = contents;
    }
}
