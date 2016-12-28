package org.mc.io;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import org.jutils.ui.fields.IDescriptor;

/**  */
public class TimeDescriptor implements IDescriptor<Long>
{
    /**  */
    private final SimpleDateFormat dateFormat;
    /**  */
    private final GregorianCalendar calendar;

    /***************************************************************************
     * 
     **************************************************************************/
    public TimeDescriptor()
    {
        this.dateFormat = new SimpleDateFormat( "yyyy.MM.dd HH:mm:ss z" );
        this.calendar = new GregorianCalendar();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getDescription( Long time )
    {
        calendar.setTimeInMillis( time );

        return dateFormat.format( calendar.getTime() );
    }

}
