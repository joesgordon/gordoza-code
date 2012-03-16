package nmrc.io;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import nmrc.model.IShiftxRecord;

import org.jutils.io.IReader;

public class ShiftxFileReader implements IReader<List<IShiftxRecord>>
{
    private ShiftxRecordReader recordReader;
    private BufferedReader reader;

    public ShiftxFileReader( Reader reader )
    {
        this.reader = new BufferedReader( reader );
        recordReader = new ShiftxRecordReader( this.reader );
    }

    @Override
    public Reader getStream()
    {
        return reader;
    }

    @Override
    public List<IShiftxRecord> read() throws IOException
    {
        List<IShiftxRecord> records = new ArrayList<IShiftxRecord>();
        IShiftxRecord record = null;

        reader.readLine();

        // System.out.println( "Read first line: " + reader.readLine() );

        while( ( record = recordReader.read() ) != null )
        {
            records.add( record );
        }

        return records;
    }

}
