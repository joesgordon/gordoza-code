package nmrc.alg;

import java.util.ArrayList;
import java.util.List;

import nmrc.PeakUtils;
import nmrc.data.DoubleMatch;
import nmrc.model.*;

/*******************************************************************************
 * Provides a standard method of comparing two peaks.
 ******************************************************************************/
public class DuplicatePeakMatcher implements IMatcher<IPeak>
{
    /**  */
    private IPeak thisPeak;
    /**  */
    private double hnTol = 0.2;
    /**  */
    private double otherTol = 0.2;

    /**  */
    private List<Double> thisBeta;
    /**  */
    private List<Double> thatBeta;

    /**  */
    private ListBuilder<Double> listBuilder;

    /***************************************************************************
     * Initializes this test with the given peak. All peak will be tested
     * against this one.
     * @param peak
     **************************************************************************/
    public DuplicatePeakMatcher( IPeak peak )
    {
        thisPeak = peak;
        listBuilder = new ListBuilder<Double>();

        thisBeta = new ArrayList<Double>();
        thatBeta = new ArrayList<Double>();
    }

    /***************************************************************************
     * Tests the provided peak for equality with this classes peak. Returns
     * <code>false</code> when the peaks are exactly equal (i.e. the same
     * object).
     * @return <code>true</code> if the peak is equal to this peak,
     * <code>false</code> otherwise.
     **************************************************************************/
    @Override
    public boolean matches( IPeak thatPeak )
    {
        IPeakRecord thisRecord = thisPeak.getRecord();
        IPeakRecord thatRecord = thatPeak.getRecord();
        DoubleMatch match;

        // ---------------------------------------------------------------------
        // Return false if they have the same name because of course those are
        // equal. I'm trying to find all the ones that isn't the one passed to
        // the constructor.
        // ---------------------------------------------------------------------
        if( thisRecord.getPeakName().equalsIgnoreCase( thatRecord.getPeakName() ) )
        {
            return false;
        }

        if( isAlreadyEqual( thatPeak ) )
        {
            return false;
        }

        if( !PeakUtils.doubleEquals( thisRecord.getHn(), thatRecord.getHn(),
            hnTol ) )
        {
            return false;
        }

        if( !PeakUtils.doubleEquals( thisRecord.getN15(), thatRecord.getN15(),
            hnTol ) )
        {
            return false;
        }

        match = PeakUtils.getBestMatch( thisRecord.getCarbonAlpha(),
            thatRecord.getCarbonAlpha() );
        if( !PeakUtils.isWithinTolerance( match, otherTol ) )
        {
            return false;
        }

        thisBeta.clear();

        listBuilder.addAllItems( thisPeak.getRecord().getCarbonBeta(), thisBeta );
        listBuilder.addAllItems( thisPeak.getRecord().getAlternateCarbonBeta(),
            thisBeta );

        thatBeta.clear();

        listBuilder.addAllItems( thatPeak.getRecord().getCarbonBeta(), thatBeta );
        listBuilder.addAllItems( thatPeak.getRecord().getAlternateCarbonBeta(),
            thatBeta );

        match = PeakUtils.getBestMatch( thisBeta, thatBeta );
        if( !PeakUtils.isWithinTolerance( match, otherTol ) )
        {
            return false;
        }

        match = PeakUtils.getBestMatch( thisRecord.getPreviousCarbonAlpha(),
            thatRecord.getPreviousCarbonAlpha() );
        if( !PeakUtils.isWithinTolerance( match, otherTol ) )
        {
            return false;
        }

        thisBeta.clear();

        listBuilder.addAllItems( thisPeak.getRecord().getPreviousCarbonBeta(),
            thisBeta );
        listBuilder.addAllItems( thisPeak.getRecord().getAlternateCarbonBeta(),
            thisBeta );

        thatBeta.clear();

        listBuilder.addAllItems( thatPeak.getRecord().getPreviousCarbonBeta(),
            thatBeta );
        listBuilder.addAllItems( thatPeak.getRecord().getAlternateCarbonBeta(),
            thatBeta );

        match = PeakUtils.getBestMatch( thisBeta, thatBeta );
        if( !PeakUtils.isWithinTolerance( match, otherTol ) )
        {
            return false;
        }

        return true;
    }

    /***************************************************************************
     * @param thatPeak
     * @return
     **************************************************************************/
    private boolean isAlreadyEqual( IPeak thatPeak )
    {
        List<IPeakRecord> thisDups = thisPeak.getRecord().getDuplicates();
        List<IPeakRecord> thatDups = thatPeak.getRecord().getDuplicates();

        if( thisDups != null && thisDups.contains( thatPeak.getRecord() ) )
        {
            return true;
        }

        if( thatDups != null && thatDups.contains( thisPeak.getRecord() ) )
        {
            return true;
        }

        return false;
    }
}
