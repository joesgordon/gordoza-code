package nmrc.io;

import java.io.*;

import nmrc.data.ShiftxRecord;
import nmrc.model.IShiftxRecord;

import org.jutils.Utils;
import org.jutils.io.IReader;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ShiftxRecordReader implements IReader<IShiftxRecord>
{
    /**  */
    private LineNumberReader reader;
    /**  */
    private EmptyDoubleParser doubleParser;

    /***************************************************************************
     * @param reader
     **************************************************************************/
    public ShiftxRecordReader( Reader reader )
    {
        this.reader = new LineNumberReader( reader );
        doubleParser = new EmptyDoubleParser();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Reader getStream()
    {
        return reader;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IShiftxRecord read() throws IOException
    {
        IShiftxRecord record = null;
        String line = reader.readLine();

        if( line != null )
        {
            String[] records = line.split( "\\s*,\\s*", 6 );

            if( records.length != 6 )
            {
                String msg = "Invalid number or records (" + records.length +
                    ") on line number " + reader.getLineNumber() + "." +
                    Utils.NEW_LINE + "Line: " + line;

                System.err.println( msg );
                for( int i = 0; i < records.length; i++ )
                {
                    System.err.println( "\t" + i + ": " + records[i] );
                }

                throw new IOException( msg );
            }

            for( int i = 0; i < records.length; i++ )
            {
                records[i] = records[i].replace( "\"", "" );
                records[i] = records[i].replace( "~", "" );
                records[i] = records[i].replace( "-", "" );
            }

            record = getRecord( records );
        }

        return record;
    }

    /***************************************************************************
     * @param values
     * @return
     * @throws IOException
     **************************************************************************/
    private IShiftxRecord getRecord( String[] values ) throws IOException
    {
        Double h = doubleParser.parseItem( values[2] );
        Double n = doubleParser.parseItem( values[3] );
        Double cA = doubleParser.parseItem( values[4] );
        Double cB = doubleParser.parseItem( values[5] );

        return new ShiftxRecord( values[0], values[1], h, n, cA, cB );
    }
}
