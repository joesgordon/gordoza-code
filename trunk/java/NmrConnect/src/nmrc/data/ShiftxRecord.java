package nmrc.data;

import nmrc.model.IShiftxRecord;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ShiftxRecord implements IShiftxRecord
{
    /**  */
    private String aaName;
    /**  */
    private String aaNumber;
    /**  */
    private Double hydrogen;
    /**  */
    private Double nitrogen;
    /**  */
    private Double cAlpha;
    /**  */
    private Double cBeta;

    public ShiftxRecord( String aminoAcidName, String aminoAcidNumber,
        Double hydrogenValue, Double nitrogenValue, Double carbonAlphaValue,
        Double carbonBetaValue )
    {
        aaName = aminoAcidName;
        aaNumber = aminoAcidNumber;
        hydrogen = hydrogenValue;
        nitrogen = nitrogenValue;
        cAlpha = carbonAlphaValue;
        cBeta = carbonBetaValue;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getAminoAcidName()
    {
        return aaName;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getAminoAcidNumber()
    {
        return aaNumber;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Double getCarbonAlpha()
    {
        return cAlpha;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Double getCarbonBeta()
    {
        return cBeta;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Double getHydrogen()
    {
        return hydrogen;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Double getNitrogen()
    {
        return nitrogen;
    }
}
