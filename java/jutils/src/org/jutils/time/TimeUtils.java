package org.jutils.time;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class TimeUtils
{
    public static final long MINUTES_IN_DAY = 60 * 24;
    public static final long SECONDS_IN_DAY = 60 * MINUTES_IN_DAY;

    public static final long MILLIS_IN_MIN = 1000 * 60;
    public static final long MILLIS_IN_HOUR = 60 * MILLIS_IN_MIN;
    public static final long MILLIS_IN_DAY = 24 * MILLIS_IN_HOUR;
    public static final long MILLIS_IN_WEEK = 7 * MILLIS_IN_DAY;

    public static final long MICROS_IN_DAY = 1000 * MILLIS_IN_DAY;

    public static final long MAX_DAYS_IN_YEAR = 366;
    public static final long MAX_HOURS_IN_YEAR = MAX_DAYS_IN_YEAR * 24;
    public static final long MAX_MIN_IN_YEAR = MAX_HOURS_IN_YEAR * 60;
    public static final long MAX_SECONDS_IN_YEAR = MAX_MIN_IN_YEAR * 60;
    public static final long MAX_MILLIS_IN_YEAR = MAX_SECONDS_IN_YEAR * 1000;
    public static final long MAX_MICROS_IN_YEAR = MAX_MILLIS_IN_YEAR * 1000;
    public static final TimeZone UTC = TimeZone.getTimeZone( "UTC" );

    private static final long LEAP_YEAR_START = yearStartMillis( 2004 );

    /***************************************************************************
     * 
     **************************************************************************/
    private TimeUtils()
    {
    }

    /***************************************************************************
     * Returns the current year as of invocation.
     * @return the current year.
     **************************************************************************/
    public static int getCurrentYear()
    {
        return Calendar.getInstance().get( Calendar.YEAR );
    }

    /***************************************************************************
     * Returns a string representing the provided microseconds in
     * {@code D HH:mm:ss.uuuuuu} format where D = days, HH = hours into day
     * (00-23), mm = minutes into hour (00-59), ss = seconds into minute
     * (00-59), uuuuuu = microseconds (000000-999999).
     * @param microseconds
     * @return
     **************************************************************************/
    public static String microsDurationToString( long microseconds )
    {
        int micros = ( int )( Math.abs( microseconds ) % 1000 );
        long millis = microseconds / 1000;

        return durationToString( millis, micros );
    }

    /***************************************************************************
     * @param milliseconds
     * @return
     **************************************************************************/
    public static String durationToString( long milliseconds )
    {
        return durationToString( milliseconds, 0 );
    }

    /***************************************************************************
     * @param milliseconds
     * @param micros
     * @return
     **************************************************************************/
    private static String durationToString( long milliseconds, int micros )
    {
        return millisToString( milliseconds ) +
            String.format( "%03d", Math.abs( micros ) );
    }

    /***************************************************************************
     * @param milliseconds
     * @return
     **************************************************************************/
    public static String millisToString( long milliseconds )
    {
        boolean negative = milliseconds < 0;
        long t = Math.abs( milliseconds );

        int millis = ( int )( t % 1000 );
        t = t / 1000; // seconds

        int sec = ( int )( t % 60 );
        t = t / 60; // min

        int min = ( int )( t % 60 );
        t = t / 60; // hours

        int hours = ( int )( t % 24 );
        t = t / 24; // days

        int days = ( int )( t );

        String str = negative ? "-" : "";

        if( days > 0 )
        {
            str += String.format( "%03d %02d:%02d:%02d.%03d", days, hours, min,
                sec, millis );
        }
        else if( hours > 0 )
        {
            str += String.format( "%02d:%02d:%02d.%03d", hours, min, sec,
                millis );
        }
        else if( min > 0 )
        {
            str += String.format( "%02d:%02d.%03d", min, sec, millis );
        }
        else if( sec > 0 )
        {
            str += String.format( "%02d.%03d", sec, millis );
        }
        else if( millis > 0 )
        {
            str += String.format( "0.%03d", millis );
        }
        else if( millis == 0 )
        {
            str += "0.000";
        }

        return str;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static Calendar today()
    {
        GregorianCalendar cal = new GregorianCalendar( UTC );

        cal.set( Calendar.HOUR_OF_DAY, 0 );
        cal.set( Calendar.MINUTE, 0 );
        cal.set( Calendar.SECOND, 0 );
        cal.set( Calendar.MILLISECOND, 0 );

        return cal;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static long todayMillisIntoYear()
    {
        return today().getTimeInMillis() - yearStartMillis();
    }

    public static short getYearOfMillis( long millis )
    {
        GregorianCalendar cal = new GregorianCalendar( UTC );

        cal.setTimeInMillis( millis );

        return ( short )cal.get( Calendar.YEAR );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static long yearStartMillis()
    {
        Calendar yearStart = today();

        yearStart.set( Calendar.DAY_OF_YEAR, 0 );

        return yearStart.getTimeInMillis();
    }

    /***************************************************************************
     * @param year
     * @return
     **************************************************************************/
    public static long yearStartMillis( int year )
    {
        GregorianCalendar cal = new GregorianCalendar( UTC );

        cal.set( Calendar.DAY_OF_YEAR, 1 );
        cal.set( Calendar.HOUR_OF_DAY, 0 );
        cal.set( Calendar.MINUTE, 0 );
        cal.set( Calendar.SECOND, 0 );
        cal.set( Calendar.MILLISECOND, 0 );
        cal.set( Calendar.YEAR, year );

        return cal.getTimeInMillis();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static long now()
    {
        return new GregorianCalendar( UTC ).getTimeInMillis() -
            yearStartMillis();
    }

    /***************************************************************************
     * @param month 1-relative month
     * @param day 1-relative day of month
     * @return milliseconds since the beginning of a leap year
     **************************************************************************/
    public static long monthDayToMillis( int month, int day )
    {
        Calendar doy = new GregorianCalendar( 2004, month - 1, day );

        long millis = doy.getTimeInMillis() - LEAP_YEAR_START;

        return millis;
    }

    /***************************************************************************
     * @param month 1-relative month
     * @param day 1-relative day of month
     * @parame year
     * @return milliseconds since the beginning of the provided year
     **************************************************************************/
    public static long monthDayToMillis( int month, int day, int year )
    {
        LocalDate date = LocalDate.of( year, month, day );
        int doy = date.getDayOfYear() - 1;
        long millis = doy * MILLIS_IN_DAY;

        return millis;
    }

    /***************************************************************************
     * @param ldt
     * @return
     **************************************************************************/
    public static LocalDateTime getBeginningOfWeek( LocalDateTime ldt )
    {
        int days = ldt.getDayOfWeek().getValue();

        days = days < 7 ? days : 0;

        LocalDate date = ldt.toLocalDate().minus( days, ChronoUnit.DAYS );

        return LocalDateTime.of( date, LocalTime.MIDNIGHT );
    }
}
