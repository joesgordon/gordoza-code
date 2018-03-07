package org.jutils.time;

import java.time.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class NanoTime
{
    /**  */
    public static final long NANOS_PER_DAY = 1000000000L * 60 * 60 * 24;

    /** The year of the time. */
    public short year;
    /**
     * The number of nanoseconds into year. A long will hold up to attoseconds
     * (10<sup>-18</sup>) for 366 days.
     */
    public long nanos;

    /***************************************************************************
     * 
     **************************************************************************/
    public NanoTime()
    {
        this.year = 0;
        this.nanos = 0;
    }

    /***************************************************************************
     * @param time
     **************************************************************************/
    public NanoTime( LocalDateTime time )
    {
        this();
        this.setLocalDateTime( time );
    }

    /***************************************************************************
     * @return the time as a {@link LocalDateTime}.
     **************************************************************************/
    public LocalDateTime toLocalDateTime()
    {
        int doy = ( int )( nanos / NANOS_PER_DAY );
        long nod = nanos - ( doy * NANOS_PER_DAY );

        doy++;

        LocalDate date = LocalDate.ofYearDay( year, doy );
        LocalTime time = LocalTime.ofNanoOfDay( nod );

        return LocalDateTime.of( date, time );
    }

    /***************************************************************************
     * @param time
     **************************************************************************/
    public void setLocalDateTime( LocalDateTime time )
    {
        this.year = ( short )time.getYear();
        int doy = time.getDayOfYear() - 1;
        this.nanos = doy * NANOS_PER_DAY + time.toLocalTime().toNanoOfDay();
    }

    public static NanoTime now()
    {
        NanoTime time = new NanoTime();

        time.setLocalDateTime( LocalDateTime.now() );

        return time;
    }
}
