package nmrc.data;

import nmrc.model.IPeak;
import nmrc.model.IPeakChoice;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PeakChoice implements IPeakChoice
{
    /**  */
    private IPeak peak;
    /**  */
    private Double[] alphaMatches;
    /**  */
    private Double[] betaMatches;

    /***************************************************************************
     * @param choice
     * @param alphas
     * @param betas
     **************************************************************************/
    public PeakChoice( IPeak choice, Double[] alphas, Double[] betas )
    {
        peak = choice;
        alphaMatches = alphas;
        betaMatches = betas;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Double[] getAlphaMatch()
    {
        return alphaMatches;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Double[] getBetaMatch()
    {
        return betaMatches;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IPeak getPeak()
    {
        return peak;
    }

}
