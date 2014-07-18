package nmrc.io;

import java.io.IOException;
import java.io.LineNumberReader;

import nmrc.data.ShiftxRecord;
import nmrc.model.IShiftxRecord;

import org.jutils.Utils;
import org.jutils.io.IReader;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ShiftxRecordReader implements
    IReader<IShiftxRecord, LineNumberReader>
{
    /**  */
    private EmptyDoubleParser doubleParser;

    /***************************************************************************
     * @param reader
     **************************************************************************/
    public ShiftxRecordReader()
    {
        doubleParser = new EmptyDoubleParser();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IShiftxRecord read( LineNumberReader reader ) throws IOException
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
        Double h = doubleParser.parse( values[2] );
        Double n = doubleParser.parse( values[3] );
        Double cA = doubleParser.parse( values[4] );
        Double cB = doubleParser.parse( values[5] );

        return new ShiftxRecord( values[0], values[1], h, n, cA, cB );
    }
}
