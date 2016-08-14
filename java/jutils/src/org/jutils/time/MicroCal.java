package org.jutils.time;

import java.time.DayOfWeek;
import java.util.*;

/*******************************************************************************
 * @param <Cal>
 ******************************************************************************/
public class MicroCal
{
    /** The date and time at the resolution of milliseconds. */
    private final GregorianCalendar calendar;
    /** The number of microseconds into the current millisecond. */
    private int microseconds;

    /***************************************************************************
     * @param calendar
     **************************************************************************/
    public MicroCal()
    {
        this( TimeUtils.UTC );
    }

    /***************************************************************************
     * @param tz
     **************************************************************************/
    public MicroCal( TimeZone tz )
    {
        this.calendar = new GregorianCalendar( tz );
        this.microseconds = 0;

        // LogUtils.printDebug( "Created: " + toString() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Calendar getCalendar()
    {
        return ( Calendar )calendar.clone();
    }

    /***************************************************************************
     * @param data
     **************************************************************************/
    public void setCalendar( Calendar data )
    {
        this.calendar.clear();
        this.calendar.setTimeZone( data.getTimeZone() );
        this.calendar.setTimeInMillis( data.getTimeInMillis() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getMicroseconds()
    {
        return microseconds;
    }

    /***************************************************************************
     * Sets the number of microseconds into the current millisecond.
     * @param micros
     **************************************************************************/
    public void setMicroseconds( int micros )
    {
        this.microseconds = micros;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public long getMicrosecondsIntoYear()
    {
        int year = calendar.get( Calendar.YEAR );
        long yearms = TimeUtils.yearStartMillis( year );

        return ( calendar.getTimeInMillis() - yearms ) * 1000 + microseconds;
    }

    /***************************************************************************
     * @param micros
     **************************************************************************/
    public void setMicrosecondsIntoYear( long micros )
    {
        long millis = micros / 1000;

        int doy = ( int )( millis / TimeUtils.MILLIS_IN_DAY );
        millis -= ( doy * TimeUtils.MILLIS_IN_DAY );

        int hour = ( int )( millis / TimeUtils.MILLIS_IN_HOUR );
        millis -= ( hour * TimeUtils.MILLIS_IN_HOUR );

        int min = ( int )( millis / TimeUtils.MILLIS_IN_MIN );
        millis -= ( min * TimeUtils.MILLIS_IN_MIN );

        int sec = ( int )( millis / 1000 );
        millis -= ( sec * 1000L );

        int ms = ( int )( millis );

        // LogUtils.printDebug( String.format( "%d/%d/%d (%d) %d:%d:%d.%d",
        // calendar.get( Calendar.MONTH ) + 1,
        // calendar.get( Calendar.DAY_OF_MONTH ),
        // calendar.get( Calendar.YEAR ), doy, hour, min, sec, ms ) );

        TimeZone tz = calendar.getTimeZone();
        int year = getYear();

        calendar.clear();
        calendar.setTimeZone( tz );
        setYear( year );
        calendar.set( Calendar.DAY_OF_YEAR, doy + 1 );
        calendar.set( Calendar.HOUR_OF_DAY, hour );
        calendar.set( Calendar.MINUTE, min );
        calendar.set( Calendar.SECOND, sec );
        calendar.set( Calendar.MILLISECOND, ms );

        // LogUtils.printDebug( String.format( "%d/%d/%d (%d) %d:%d:%d.%d",
        // calendar.get( Calendar.MONTH ) + 1,
        // calendar.get( Calendar.DAY_OF_MONTH ),
        // calendar.get( Calendar.YEAR ), doy, hour, min, sec, ms ) );

        microseconds = ( int )( micros % 1000L );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getYear()
    {
        return calendar.get( Calendar.YEAR );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getDayOfYear()
    {
        return calendar.get( Calendar.DAY_OF_YEAR );
    }

    /***************************************************************************
     * @param doy
     **************************************************************************/
    public void setDayOfYear( int doy )
    {
        calendar.set( Calendar.DAY_OF_YEAR, doy );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public double getDaysIntoYear()
    {
        return getMicrosecondsIntoYear() / ( double )TimeUtils.MICROS_IN_DAY;
    }

    /***************************************************************************
     * @param diy
     **************************************************************************/
    public void setDaysIntoYear( double diy )
    {
        double frac;
        int doy;
        int hour;
        int min;
        int sec;
        int ms;

        frac = diy;
        doy = ( int )frac + 1;

        frac = ( frac - doy + 1 ) * 24;
        hour = ( int )frac;

        frac = ( frac - hour ) * 60;
        min = ( int )frac;

        frac = ( frac - min ) * 60;
        sec = ( int )frac;

        frac = ( frac - sec ) * 1000;
        ms = ( int )frac;

        frac = ( frac - ms ) * 1000;
        microseconds = ( int )frac;

        setDayOfYear( doy );
        calendar.set( Calendar.HOUR_OF_DAY, hour );
        calendar.set( Calendar.MINUTE, min );
        calendar.set( Calendar.SECOND, sec );
        calendar.set( Calendar.MILLISECOND, ms );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public DayOfWeek getDayOfWeek()
    {
        // LogUtils.printDebug( "getDayOfWeek: " + toString() );

        int dow = calendar.get( Calendar.DAY_OF_WEEK ) - 1;

        dow = dow < 1 ? 7 : dow;

        return DayOfWeek.of( dow );
    }

    /***************************************************************************
     * @param dow
     **************************************************************************/
    public void setDayOfWeek( DayOfWeek dow )
    {
        int index = dow.getValue() + 1;

        index = index > 7 ? 1 : index;

        calendar.set( Calendar.DAY_OF_WEEK, index );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public long getMicrosecondsIntoWeek()
    {
        Calendar weekStart = ( Calendar )calendar.clone();

        weekStart.set( Calendar.DAY_OF_WEEK, Calendar.SUNDAY );
        weekStart.set( Calendar.HOUR_OF_DAY, 0 );
        weekStart.set( Calendar.MINUTE, 0 );
        weekStart.set( Calendar.SECOND, 0 );
        weekStart.set( Calendar.MILLISECOND, 0 );

        long micros = ( calendar.getTimeInMillis() -
            weekStart.getTimeInMillis() ) * 1000;

        return ( micros + microseconds );
    }

    /***************************************************************************
     * @param siw
     **************************************************************************/
    public void setMicrosecondsIntoWeek( long microsIntoWeek )
    {
        double frac;
        int dow;
        int hour;
        int min;
        int sec;
        int ms;

        frac = microsIntoWeek / 3600000000.0 / 24.0;
        dow = ( int )frac;

        frac = ( frac - dow ) * 24;
        hour = ( int )frac;

        frac = ( frac - hour ) * 60;
        min = ( int )frac;

        frac = ( frac - min ) * 60;
        sec = ( int )frac;

        frac = ( frac - sec ) * 1000;
        ms = ( int )frac;

        frac = ( frac - ms ) * 1000;
        microseconds = ( int )frac;

        dow = dow < 1 ? dow + 7 : dow;
        setDayOfWeek( DayOfWeek.of( dow ) );
        calendar.set( Calendar.HOUR_OF_DAY, hour );
        calendar.set( Calendar.MINUTE, min );
        calendar.set( Calendar.SECOND, sec );
        calendar.set( Calendar.MILLISECOND, ms );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public long getMicrosecondsIntoDay()
    {
        Calendar weekStart = ( Calendar )calendar.clone();

        weekStart.set( Calendar.HOUR_OF_DAY, 0 );
        weekStart.set( Calendar.MINUTE, 0 );
        weekStart.set( Calendar.SECOND, 0 );
        weekStart.set( Calendar.MILLISECOND, 0 );

        long micros = ( calendar.getTimeInMillis() -
            weekStart.getTimeInMillis() ) * 1000 + microseconds;

        /// 1000000.0
        return micros;
    }

    /***************************************************************************
     * @param from
     **************************************************************************/
    public void setMicrosecondsIntoDay( long sid )
    {
        long us = getMicrosecondsIntoYear();

        us = us / TimeUtils.MILLIS_IN_DAY / 1000;
        us *= TimeUtils.MILLIS_IN_DAY * 1000;

        us = us + sid;

        setMicrosecondsIntoYear( us );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public double getHoursIntoDay()
    {
        int hour = calendar.get( Calendar.HOUR_OF_DAY );
        int min = calendar.get( Calendar.MINUTE );
        int sec = calendar.get( Calendar.SECOND );
        int ms = calendar.get( Calendar.MILLISECOND );
        long us = 1000 * ( hour * TimeUtils.MILLIS_IN_HOUR +
            min * TimeUtils.MILLIS_IN_MIN + sec * 1000L + ms ) + microseconds;

        return us / 1000.0 / TimeUtils.MILLIS_IN_HOUR;
    }

    /***************************************************************************
     * @param from
     **************************************************************************/
    public void setHoursIntoDay( Double hid )
    {
        double frac;
        int hour;
        int min;
        int sec;
        int ms;

        frac = hid / 3600.0;
        hour = ( int )frac;

        frac = ( frac - hour ) * 60;
        min = ( int )frac;

        frac = ( frac - min ) * 60;
        sec = ( int )frac;

        frac = ( frac - sec ) * 1000;
        ms = ( int )frac;

        frac = ( frac - ms ) * 1000;
        microseconds = ( int )frac;

        calendar.set( Calendar.HOUR_OF_DAY, hour );
        calendar.set( Calendar.MINUTE, min );
        calendar.set( Calendar.SECOND, sec );
        calendar.set( Calendar.MILLISECOND, ms );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getGpsWeek()
    {
        // 00:00:00.000 06 January 1980
        // http://www.usno.navy.mil/USNO/time/gps/gps-week-number-rollover
        GregorianCalendar gc = new GregorianCalendar();

        gc.clear();
        gc.setTimeZone( TimeUtils.UTC );
        gc.set( 1980, Calendar.JANUARY, 6 );

        long ms = calendar.getTimeInMillis() - gc.getTimeInMillis();
        long week = ( long )( ms / TimeUtils.MILLIS_IN_WEEK );

        return ( int )( week );
    }

    /***************************************************************************
     * @param week
     **************************************************************************/
    public void setGpsWeek( int week )
    {
        GregorianCalendar gc = new GregorianCalendar();

        gc.clear();
        gc.setTimeZone( TimeUtils.UTC );
        gc.set( 1980, Calendar.JANUARY, 6 );

        long millis = week * TimeUtils.MILLIS_IN_WEEK + gc.getTimeInMillis();

        TimeZone tz = getTimeZone();
        DayOfWeek dow = getDayOfWeek();
        int hour = calendar.get( Calendar.HOUR_OF_DAY );
        int min = calendar.get( Calendar.MINUTE );
        int sec = calendar.get( Calendar.SECOND );
        int ms = calendar.get( Calendar.MILLISECOND );

        calendar.clear();
        calendar.setTimeZone( tz );
        calendar.setTimeInMillis( millis );
        setDayOfWeek( dow );
        calendar.set( Calendar.HOUR_OF_DAY, hour );
        calendar.set( Calendar.MINUTE, min );
        calendar.set( Calendar.SECOND, sec );
        calendar.set( Calendar.MILLISECOND, ms );
    }

    /***************************************************************************
     * @param data
     **************************************************************************/
    public void set( MicroCal data )
    {
        this.microseconds = data.microseconds;

        setCalendar( data.calendar );
    }

    /***************************************************************************
     * @param year
     **************************************************************************/
    public void setYear( int year )
    {
        calendar.set( Calendar.YEAR, year );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public TimeZone getTimeZone()
    {
        return calendar.getTimeZone();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Date getTime()
    {
        return calendar.getTime();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String toString()
    {
        int year = calendar.get( Calendar.YEAR );
        int month = calendar.get( Calendar.MONTH ) + 1;
        int dom = calendar.get( Calendar.DAY_OF_MONTH );

        int hour = calendar.get( Calendar.HOUR_OF_DAY );
        int min = calendar.get( Calendar.MINUTE );
        int sec = calendar.get( Calendar.SECOND );
        int ms = calendar.get( Calendar.MILLISECOND );

        String str = String.format( "%02d/%02d/%04d %02d:%02d:%02d.%03d%03d",
            month, dom, year, hour, min, sec, ms, microseconds );

        return str;
    }

    /***************************************************************************
     * @param month the 1-relative month of the year.
     * @param day the 1-relative day of the month.
     * @param year the year.
     **************************************************************************/
    public void setMDY( int month, int day, int year )
    {
        Calendar cal = getCalendar();

        cal.set( year, month - 1, day );

        setCalendar( cal );
    }
}
