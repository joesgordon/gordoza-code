package nmrc.io;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import nmrc.model.IAminoAcid;
import nmrc.model.IPeak;

import org.jutils.io.IWriter;

/*******************************************************************************
 * 
 ******************************************************************************/
public class AminoAcidsCsvWriter implements
    IWriter<List<IAminoAcid>, PrintWriter>
{

    /***************************************************************************
     * @param writer
     **************************************************************************/
    public AminoAcidsCsvWriter()
    {
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( List<IAminoAcid> aas, PrintWriter outStream )
        throws IOException
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

            writeValue( aa.getShiftX().getAminoAcidNumber(), outStream );
            outStream.print( "," );

            writeValue( aa.getShiftX().getAminoAcidName(), outStream );
            outStream.print( "," );

            if( peak != null )
            {
                writeValue( peak.getRecord().getPeakName(), outStream );
                outStream.print( "," );

                writeValue( peak.getAlpha(), outStream );
                outStream.print( "," );

                writeValue( peak.getBeta(), outStream );
                outStream.print( "," );

                writeValue( peak.getPreviousAlpha(), outStream );
                outStream.print( "," );

                writeValue( peak.getPreviousBeta(), outStream );
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
    private void writeValue( Object value, PrintWriter outStream )
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
