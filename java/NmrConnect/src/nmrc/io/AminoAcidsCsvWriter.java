package nmrc.io;

import java.io.*;
import java.util.List;

import nmrc.model.IAminoAcid;
import nmrc.model.IPeak;

import org.jutils.io.IWriter;

/*******************************************************************************
 * 
 ******************************************************************************/
public class AminoAcidsCsvWriter implements IWriter<List<IAminoAcid>>
{
    /**  */
    private Writer writer;
    /**  */
    private PrintWriter outStream;

    /***************************************************************************
     * @param writer
     **************************************************************************/
    public AminoAcidsCsvWriter( Writer writer )
    {
        this.writer = writer;
        outStream = new PrintWriter( writer );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Writer getStream()
    {
        return writer;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( List<IAminoAcid> aas ) throws IOException
    {
        outStream.print( "AA #" );
        outStream.print( "," );

        outStream.print( "AA" );
        outStream.print( "," );

        outStream.print( "Peak Name" );
        outStream.print( "," );

        outStream.print( "Ca" );
        outStream.print( "," );

        outStream.print( "Cb" );
        outStream.print( "," );

        outStream.print( "Ca[i-1]" );
        outStream.print( "," );

        outStream.print( "Cb[i-1]" );
        outStream.println();

        for( IAminoAcid aa : aas )
        {
            IPeak peak = aa.getPeak();

            writeValue( aa.getShiftX().getAminoAcidNumber() );
            outStream.print( "," );

            writeValue( aa.getShiftX().getAminoAcidName() );
            outStream.print( "," );

            if( peak != null )
            {
                writeValue( peak.getRecord().getPeakName() );
                outStream.print( "," );

                writeValue( peak.getAlpha() );
                outStream.print( "," );

                writeValue( peak.getBeta() );
                outStream.print( "," );

                writeValue( peak.getPreviousAlpha() );
                outStream.print( "," );

                writeValue( peak.getPreviousBeta() );
            }
            else
            {
                outStream.print( "," );
                outStream.print( "," );
                outStream.print( "," );
                outStream.print( "," );
            }

            outStream.println();
        }

        outStream.close();
    }

    /***************************************************************************
     * @param value
     **************************************************************************/
    private void writeValue( Object value )
    {
        String str = "";

        if( value != null )
        {
            str = value.toString();

            if( str.contains( "," ) )
            {
                str = "\"" + str + "\"";
            }
        }

        outStream.print( str );
    }
}
