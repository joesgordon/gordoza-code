package testbed;

import java.text.SimpleDateFormat;
import java.util.*;

import org.jutils.io.LogUtils;

public class CalcWorkDays
{
    public static int MILLIS_IN_DAY = 1000 * 60 * 60 * 24;

    public static SimpleDateFormat sdf = new SimpleDateFormat(
        "yyyy-MM-dd HH:mm:ss z" );

    /***************************************************************************
     * @param startDate
     * @param endDate
     * @return
     **************************************************************************/
    public static int getWorkingDaysBetweenTwoDates( Date startDate,
        Date endDate )
    {
        Calendar startCal;
        Calendar endCal;
        startCal = Calendar.getInstance();
        startCal.setTime( startDate );
        endCal = Calendar.getInstance();
        endCal.setTime( endDate );
        int workDays = 0;

        // Return 0 if start and end are the same
        if( startCal.getTimeInMillis() == endCal.getTimeInMillis() )
        {
            return 0;
        }

        if( startCal.getTimeInMillis() > endCal.getTimeInMillis() )
        {
            startCal.setTime( endDate );
            endCal.setTime( startDate );
        }

        do
        {
            startCal.add( Calendar.DAY_OF_MONTH, 1 );
            if( startCal.get( Calendar.DAY_OF_WEEK ) != Calendar.SATURDAY &&
                startCal.get( Calendar.DAY_OF_WEEK ) != Calendar.SUNDAY )
            {
                ++workDays;
            }
        } while( startCal.getTimeInMillis() < endCal.getTimeInMillis() );

        return workDays;
    }

    /***************************************************************************
     * @param start
     * @param end
     * @return
     **************************************************************************/
    public static int days( Date start, Date end )
    {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime( start );
        int dow1 = startCal.get( Calendar.DAY_OF_WEEK );
        startCal.add( Calendar.DAY_OF_WEEK, -dow1 );

        Calendar endCal = Calendar.getInstance();
        endCal.setTime( end );
        int dow2 = endCal.get( Calendar.DAY_OF_WEEK );
        endCal.add( Calendar.DAY_OF_WEEK, -dow2 );

        printNumDays( 0, startCal, endCal );

        // end Saturday to start Saturday
        int days = ( int )( ( endCal.getTimeInMillis() -
            startCal.getTimeInMillis() ) / MILLIS_IN_DAY );
        days = days - ( days * 2 / 7 );

        return days - dow1 + dow2;
    }

    /***************************************************************************
     * @param startDate
     * @param endDate
     * @return
     **************************************************************************/
    public static int getWorkingDays( Calendar startDate, Calendar endDate )
    {
        long startMillis = startDate.getTimeInMillis();
        int startOffset = startDate.getTimeZone().getOffset( startMillis );
        startMillis += startOffset;

        long endMillis = endDate.getTimeInMillis();
        int endOffset = endDate.getTimeZone().getOffset( endMillis );
        endMillis += endOffset;

        long startDay = startMillis / MILLIS_IN_DAY;
        long endDay = endMillis / MILLIS_IN_DAY;

        // int numDays = ( int )( ( endMillis - startMillis ) / MILLIS_IN_DAY );
        int numDays = ( int )( endDay - startDay );

        switch( startDate.get( Calendar.DAY_OF_WEEK ) )
        {
            case Calendar.SUNDAY:
                numDays -= 2;
                break;
            case Calendar.SATURDAY:
                numDays--;
                break;
        }

        switch( endDate.get( Calendar.DAY_OF_WEEK ) )
        {
            case Calendar.SUNDAY:
                numDays -= 2;
                break;
            case Calendar.SATURDAY:
                numDays--;
                break;
        }

        numDays -= ( numDays * 2 / 7 );

        return numDays;
    }

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String[] args )
    {
        GregorianCalendar start;
        GregorianCalendar end;

        // ---------------------------------------------------------------------
        // The answer should be 0.
        // ---------------------------------------------------------------------
        start = new GregorianCalendar( 1970, 0, 1, 21, 27, 0 );
        end = new GregorianCalendar( 1970, 0, 1, 18, 0, 0 );
        testDates( start, end );

        // ---------------------------------------------------------------------
        // The answer should be -1.
        // ---------------------------------------------------------------------
        start = new GregorianCalendar( 2011, 0, 5, 21, 27, 0 );
        end = new GregorianCalendar( 2011, 0, 4 );
        testDates( start, end );

        // ---------------------------------------------------------------------
        // The answer should be 5.
        // ---------------------------------------------------------------------
        start = new GregorianCalendar();
        end = ( GregorianCalendar )start.clone();
        end.add( Calendar.DAY_OF_YEAR, 7 );
        testDates( start, end );

        // ---------------------------------------------------------------------
        // The answer should be 1.
        // ---------------------------------------------------------------------
        start = new GregorianCalendar( 2011, 0, 4, 21, 27, 0 );
        end = new GregorianCalendar( 2011, 0, 5 );
        testDates( start, end );

        // ---------------------------------------------------------------------
        // The answer should be 8.
        // ---------------------------------------------------------------------
        start = new GregorianCalendar( 2011, 0, 4, 21, 27, 0 );
        end = new GregorianCalendar( 2011, 0, 16 );
        testDates( start, end );

        // ---------------------------------------------------------------------
        // The answer should be 533.
        // ---------------------------------------------------------------------
        start = new GregorianCalendar();
        end.set( 2013, 0, 20 );
        testDates( start, end );
    }

    /***************************************************************************
     * @param start
     * @param end
     **************************************************************************/
    public static void testDates( Calendar start, Calendar end )
    {
        int numDays;

        numDays = getWorkingDaysBetweenTwoDates( start.getTime(),
            end.getTime() );
        printNumDays( numDays, start, end );

        numDays = getWorkingDays( start, end );
        printNumDays( numDays, start, end );

        numDays = days( start.getTime(), end.getTime() );
        printNumDays( numDays, start, end );

        LogUtils.printDebug( "" );
    }

    /***************************************************************************
     * @param numDays
     * @param start
     * @param end
     **************************************************************************/
    public static void printNumDays( int numDays, Calendar start, Calendar end )
    {
        String msg = "The number of days between ";

        sdf.setTimeZone( start.getTimeZone() );
        msg += sdf.format( start.getTime() ) + " and ";

        sdf.setTimeZone( end.getTimeZone() );
        msg += sdf.format( end.getTime() ) + " is " + numDays;

        LogUtils.printDebug( msg );
    }
}
