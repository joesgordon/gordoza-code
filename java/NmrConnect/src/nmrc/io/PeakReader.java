package nmrc.io;

import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

import nmrc.data.PeakRecord;
import nmrc.model.IPeakRecord;

import org.jutils.Utils;
import org.jutils.io.IReader;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PeakReader implements IReader<IPeakRecord, LineNumberReader>
{
    /**  */
    private EmptyDoubleParser doubleParser;
    /**  */
    private int index;

    /***************************************************************************
     * @param reader
     **************************************************************************/
    public PeakReader()
    {
        doubleParser = new EmptyDoubleParser();
        index = 0;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IPeakRecord read( LineNumberReader reader ) throws IOException
    {
        PeakRecord peak = null;
        String line = reader.readLine();

        if( line != null )
        {
            String[] records = line.split( "\\s*,\\s*", 8 );

            // LogUtils.printDebug( "Line " + reader.getLineNumber() + ": " +
            // line );

            if( records.length != 8 )
            {
                String msg = "Invalid number or records (" + records.length +
                    ") on line number " + reader.getLineNumber() + "." +
                    Utils.NEW_LINE + "Line: " + line;
                System.err.println( msg );
                int i = 0;
                for( String rec : records )
                {
                    System.err.println( "\t" + i + ": " + rec );
                    i++;
                }
                throw new IOException( msg );
            }

            int i = 0;
            for( String rec : records )
            {
                records[i] = rec.replace( "\"", "" );
                i++;
            }

            String name = records[0];

            double hn = doubleParser.parseItem( records[1] );
            double n15 = doubleParser.parseItem( records[2] );

            List<Double> alphas = getDoubleList( records[3] );
            List<Double> betas = getDoubleList( records[4] );

            List<Double> previousAlphas = getDoubleList( records[5] );
            List<Double> previousBetas = getDoubleList( records[6] );

            List<Double> alternateBetas = getDoubleList( records[7] );

            peak = new PeakRecord( index, name, hn, n15, alphas, betas,
                previousAlphas, previousBetas, alternateBetas );
            index++;
        }

        return peak;

    }

    /***************************************************************************
     * @param list
     * @return
     * @throws IOException
     **************************************************************************/
    private List<Double> getDoubleList( String list ) throws IOException
    {
        List<Double> dList = null;

        if( list.contains( "/" ) )
        {
            String[] nums = list.split( "\\s*/\\s*", -1 );
            if( nums.length > 0 )
            {
                dList = new ArrayList<Double>( nums.length );
                for( String num : nums )
                {
                    dList.add( doubleParser.parseItem( num ) );
                }
            }
        }
        else if( list.length() > 0 )
        {
            dList = new ArrayList<Double>( 1 );
            dList.add( doubleParser.parseItem( list ) );
        }

        return dList;
    }
}
