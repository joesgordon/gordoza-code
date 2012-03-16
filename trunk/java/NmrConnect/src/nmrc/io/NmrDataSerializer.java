package nmrc.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import nmrc.model.*;

import org.jutils.io.XStreamSerializer;

/*******************************************************************************
 * 
 ******************************************************************************/
public class NmrDataSerializer extends XStreamSerializer<INmrData>
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public INmrData read( InputStream stream ) throws IOException
    {
        INmrData data = super.read( stream );

        List<IAminoAcid> aminoAcids = data.getAminoAcids();

        for( IAminoAcid aminoAcid : aminoAcids )
        {
            fixAminoAcidPeak( aminoAcid, data );
        }

        return data;
    }

    /***************************************************************************
     * @param aa
     * @param data
     **************************************************************************/
    private void fixAminoAcidPeak( IAminoAcid aa, INmrData data )
    {
        IPeak peak = aa.getPeak();

        if( peak != null )
        {
            int idx = data.getPeaks().indexOf( peak );

            // Do not check for idx < 0 because idx had better be valid.
            aa.setPeak( data.getPeaks().get( idx ) );
        }
    }
}
