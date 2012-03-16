package nmrc.alg;

import java.util.ArrayList;
import java.util.List;

import nmrc.PeakUtils;
import nmrc.data.DoubleMatch;
import nmrc.model.IMatcher;
import nmrc.model.IPeak;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PreviousPeakMatcher implements IMatcher<IPeak>
{
    /**  */
    private IPeak thisPeak;
    /**  */
    private double tolerance = 0.5;
    /**  */
    private boolean ignoreTolerance;
    /**  */
    private boolean ignorePreviousAssignments;
    /**  */
    private DoubleMatch alphaMatch;
    /**  */
    private DoubleMatch betaMatch;

    /**  */
    private List<Double> thisBeta;
    /**  */
    private List<Double> thatBeta;

    /**  */
    private ListBuilder<Double> listBuilder;

    /***************************************************************************
     * @param peak
     **************************************************************************/
    public PreviousPeakMatcher( IPeak peak )
    {
        this( peak, false );
    }

    /***************************************************************************
     * @param peak
     **************************************************************************/
    public PreviousPeakMatcher( IPeak peak, boolean ignoreTol )
    {
    }

    /***************************************************************************
     * @param peak
     **************************************************************************/
    public PreviousPeakMatcher( IPeak peak, boolean ignoreTol,
        boolean ignorePrevious )
    {
        thisPeak = peak;
        ignoreTolerance = ignoreTol;
        ignorePreviousAssignments = ignorePrevious;

        thisBeta = new ArrayList<Double>();
        thatBeta = new ArrayList<Double>();

        listBuilder = new ListBuilder<Double>();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean matches( IPeak thatPeak )
    {
        alphaMatch = null;
        betaMatch = null;

        // ---------------------------------------------------------------------
        // Return false if they have the same name because of course those are
        // equal. I'm trying to find all the ones that isn't the one passed to
        // the constructor.
        // ---------------------------------------------------------------------
        if( thisPeak.getRecord().getPeakName().equalsIgnoreCase(
            thatPeak.getRecord().getPeakName() ) )
        {
            return false;
        }

        // ---------------------------------------------------------------------
        // Return false if that peak already has been assigned.
        // ---------------------------------------------------------------------
        if( !ignorePreviousAssignments && thatPeak.isPrevious() )
        {
            return false;
        }

        alphaMatch = PeakUtils.getBestMatch(
            thisPeak.getRecord().getPreviousCarbonAlpha(),
            thatPeak.getRecord().getCarbonAlpha() );

        if( !ignoreTolerance &&
            !PeakUtils.isWithinTolerance( alphaMatch, tolerance ) )
        {
            return false;
        }

        thisBeta.clear();

        listBuilder.addAllItems( thisPeak.getRecord().getPreviousCarbonBeta(),
            thisBeta );
        listBuilder.addAllItems( thisPeak.getRecord().getAlternateCarbonBeta(),
            thisBeta );

        thatBeta.clear();

        listBuilder.addAllItems( thatPeak.getRecord().getCarbonBeta(), thatBeta );
        listBuilder.addAllItems( thatPeak.getRecord().getAlternateCarbonBeta(),
            thatBeta );

        if( thisBeta.size() > 0 )
        {
            betaMatch = PeakUtils.getBestMatch( thisBeta.size() > 0 ? thisBeta
                : null, thatBeta.size() > 0 ? thatBeta : null );

            if( !ignoreTolerance &&
                !PeakUtils.isWithinTolerance( betaMatch, tolerance ) )
            {
                return false;
            }
        }

        return true;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public DoubleMatch getAlphaMatch()
    {
        return alphaMatch;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public DoubleMatch getBetaMatch()
    {
        return betaMatch;
    }
}
