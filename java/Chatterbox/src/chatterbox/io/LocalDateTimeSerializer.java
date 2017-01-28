package chatterbox.io;

import java.io.IOException;
import java.time.*;
import java.time.temporal.ChronoField;

import org.jutils.ValidationException;
import org.jutils.io.IDataSerializer;
import org.jutils.io.IDataStream;

public class LocalDateTimeSerializer implements IDataSerializer<LocalDateTime>
{
    @Override
    public LocalDateTime read( IDataStream stream )
        throws IOException, ValidationException
    {
        int year = stream.readInt();
        int doy = stream.readInt();
        long nod = stream.readLong();

        LocalDate date = LocalDate.ofYearDay( year, doy );
        LocalTime time = LocalTime.ofNanoOfDay( nod );

        return LocalDateTime.of( date, time );
    }

    @Override
    public void write( LocalDateTime data, IDataStream stream )
        throws IOException
    {
        LocalDate date = data.toLocalDate();
        LocalTime time = data.toLocalTime();

        int year = date.getYear();
        int doy = date.getDayOfYear();
        long nod = time.getLong( ChronoField.NANO_OF_DAY );

        stream.writeInt( year );
        stream.writeInt( doy );
        stream.writeLong( nod );
    }
}
