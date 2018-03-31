package org.jutils.io;

import java.io.IOException;
import java.time.*;

/*******************************************************************************
 * Defines an {@link IDataSerializer} that reads/writes {@link LocalDateTime}s.
 ******************************************************************************/
public class LocalDateTimeSerializer implements IDataSerializer<LocalDateTime>
{
    /**  */
    public static final long NANOS_PER_DAY = 1000000000L * 60 * 60 * 24;

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public LocalDateTime read( IDataStream stream ) throws IOException
    {
        int year = stream.readShort();
        long noy = stream.readLong();

        int doy = ( int )( noy / NANOS_PER_DAY );
        long nod = noy - ( doy * NANOS_PER_DAY );

        doy++;

        // LogUtils.printDebug( "Read year %d, nanos %d -> doy %d, nod %d ",
        // year,
        // noy, doy, nod );

        LocalDate date = LocalDate.ofYearDay( year, doy );
        LocalTime time = LocalTime.ofNanoOfDay( nod );

        return LocalDateTime.of( date, time );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( LocalDateTime time, IDataStream stream )
        throws IOException
    {
        int year = time.getYear();
        int doy = time.getDayOfYear() - 1;
        long noy = doy * NANOS_PER_DAY + time.toLocalTime().toNanoOfDay();

        // LogUtils.printDebug( "Wrote year %d, nanos %d for day of year %d",
        // year,
        // noy, doy + 1 );

        stream.writeShort( ( short )year );
        stream.writeLong( noy );
    }
}
