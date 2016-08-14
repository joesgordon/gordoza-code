package org.jutils.time;

import java.util.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DayOfYear implements Comparable<DayOfYear>
{
    /** Year. */
    private int year;
    /** Day of year. */
    private int days;

    /***************************************************************************
     * 
     **************************************************************************/
    public DayOfYear()
    {
        this( TimeUtils.today() );
    }

    public DayOfYear( Calendar cal )
    {
        year = cal.get( Calendar.YEAR );
        days = cal.get( Calendar.DAY_OF_YEAR );
    }

    public DayOfYear( DayOfYear orig )
    {
        this.year = orig.year;
        this.days = orig.days;
    }

    public Calendar toCalendar()
    {
        GregorianCalendar cal = new GregorianCalendar();

        cal.clear();
        cal.set( year, 0, 1 );
        cal.set( Calendar.DAY_OF_YEAR, days );

        return cal;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int compareTo( DayOfYear that )
    {
        if( this.year < that.year )
        {
            return -1;
        }

        if( this.year > that.year )
        {
            return 1;
        }

        if( this.days < that.days )
        {
            return -1;
        }

        if( this.days > that.days )
        {
            return 1;
        }

        return 0;
    }

    public void addDays( int days )
    {
        this.days += days;
    }

    public Date toDate()
    {
        return toCalendar().getTime();
    }

    public int getDelta( DayOfYear doy )
    {
        int days;

        long millis = this.toCalendar().getTimeInMillis() -
            doy.toCalendar().getTimeInMillis();

        millis = millis / TimeUtils.MILLIS_IN_DAY;

        days = ( int )millis;

        return days;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int hashCode()
    {
        return year * 366 + days;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean equals( Object obj )
    {
        if( obj == null )
        {
            return false;
        }

        if( obj instanceof DayOfYear )
        {
            DayOfYear that = ( DayOfYear )obj;

            return that.days == days && that.year == year;
        }

        return false;
    }
}
