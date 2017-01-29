package org.mc;

import java.time.LocalDateTime;

/*******************************************************************************
 * 
 ******************************************************************************/
public class McMessage
{
    /** Time of transmission or reception. */
    public final LocalDateTime time;
    /**  */
    public final byte[] contents;

    /***************************************************************************
     * 
     **************************************************************************/
    public McMessage( byte[] contents )
    {
        this.time = LocalDateTime.now();
        this.contents = contents;
    }
}
