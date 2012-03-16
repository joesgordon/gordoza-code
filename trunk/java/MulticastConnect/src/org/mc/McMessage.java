package org.mc;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/*******************************************************************************
 * 
 ******************************************************************************/
public class McMessage
{
    /**  */
    public boolean selfMessage;

    /**  */
    public byte[] contents;

    /**  */
    public String address;

    /**  */
    public int port;

    /** Time in millis */
    public long time;

    /**  */
    private GregorianCalendar calendar;

    /**  */
    private SimpleDateFormat dateFormat;

    /***************************************************************************
     * 
     **************************************************************************/
    public McMessage()
    {
        calendar = new GregorianCalendar();
        dateFormat = new SimpleDateFormat( "yyyy.MM.dd HH:mm:ss z" );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String getDateTime()
    {
        calendar.setTimeInMillis( time );

        return dateFormat.format( calendar.getTime() );
    }
}
