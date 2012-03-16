package nmrc.data;

import java.util.List;

import nmrc.model.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class NmrData implements INmrData
{
    /**  */
    private List<IPeak> peaks;
    /**  */
    private List<IShiftxRecord> shiftx;
    /**  */
    private List<IAminoAcid> aminoAcids;

    /***************************************************************************
     * @param peaks
     * @param shiftx
     * @param aminoAcids
     **************************************************************************/
    public NmrData( List<IPeak> peaks, List<IShiftxRecord> shiftx,
        List<IAminoAcid> aminoAcids )
    {
        this.peaks = peaks;
        this.shiftx = shiftx;
        this.aminoAcids = aminoAcids;
    }

    /***************************************************************************
     *
     **************************************************************************/
    @Override
    public List<IPeak> getPeaks()
    {
        return peaks;
    }

    /***************************************************************************
     *
     **************************************************************************/
    @Override
    public List<IAminoAcid> getAminoAcids()
    {
        return aminoAcids;
    }

    /***************************************************************************
     *
     **************************************************************************/
    @Override
    public List<IShiftxRecord> getShiftX()
    {
        return shiftx;
    }
}
