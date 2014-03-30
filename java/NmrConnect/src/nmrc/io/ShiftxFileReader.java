package nmrc.io;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import nmrc.model.IShiftxRecord;

import org.jutils.io.IReader;

public class ShiftxFileReader implements
    IReader<List<IShiftxRecord>, LineNumberReader>
{
    private ShiftxRecordReader recordReader;

    public ShiftxFileReader( Reader reader )
    {
        recordReader = new ShiftxRecordReader();
    }

    @Override
    public List<IShiftxRecord> read( LineNumberReader reader )
        throws IOException
    {
        List<IShiftxRecord> records = new ArrayList<IShiftxRecord>();
        IShiftxRecord record = null;

        reader.readLine();

        // LogUtils.printDebug( "Read first line: " + reader.readLine() );

        while( ( record = recordReader.read( reader ) ) != null )
        {
            records.add( record );
        }

        return records;
    }

}
