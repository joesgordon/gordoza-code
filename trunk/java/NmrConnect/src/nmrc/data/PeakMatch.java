package nmrc.data;

import nmrc.model.IPeak;
import nmrc.model.IPeakMatch;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PeakMatch implements IPeakMatch
{
    /**  */
    private IPeak peak;
    /**  */
    private DoubleMatch alphaMatch;
    /**  */
    private DoubleMatch betaMatch;
    /**  */
    private Double overallDiff;

    /***************************************************************************
     * @param p
     * @param a
     * @param b
     * @param pa
     * @param pb
     **************************************************************************/
    public PeakMatch( IPeak peak, DoubleMatch alphaMatch, DoubleMatch betaMatch )
    {
        this.peak = peak;
        this.alphaMatch = alphaMatch;
        this.betaMatch = betaMatch;

        Double alphaDiff = alphaMatch == null ? null : alphaMatch.getDelta();
        Double betaDiff = betaMatch == null ? null : betaMatch.getDelta();

        if( alphaDiff != null && betaDiff != null )
        {
            this.overallDiff = ( alphaDiff + betaDiff ) / 2.0;
        }
        else if( alphaDiff != null )
        {
            this.overallDiff = alphaDiff;
        }
        else
        {
            this.overallDiff = betaDiff;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Double getOverallDiff()
    {
        return overallDiff;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public DoubleMatch getAlphaMatch()
    {
        return alphaMatch;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public DoubleMatch getBetaMatch()
    {
        return betaMatch;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IPeak getPeak()
    {
        return peak;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Double getAlpha()
    {
        return alphaMatch != null ? alphaMatch.getBase() : null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Double getBeta()
    {
        return betaMatch != null ? betaMatch.getBase() : null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Double getPreviousAlpha()
    {
        return alphaMatch != null ? alphaMatch.getOther() : null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Double getPreviousBeta()
    {
        return betaMatch != null ? betaMatch.getOther() : null;
    }
}
