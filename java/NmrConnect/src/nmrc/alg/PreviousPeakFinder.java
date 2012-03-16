package nmrc.alg;

import java.util.*;

import nmrc.data.DoubleMatch;
import nmrc.data.PeakMatch;
import nmrc.model.IPeak;

/***************************************************************************
 * 
 **************************************************************************/
public class PreviousPeakFinder
{
    /***************************************************************************
     * @param peak
     * @param peaks
     * @return
     **************************************************************************/
    public List<PeakMatch> findPeaks( IPeak peak, List<IPeak> peaks )
    {
        return findPeaks( peak, peaks, false );
    }

    /***************************************************************************
     * Finds all potential previous peak matches for the given peak.
     * @param peak The peak for which the previous will be found.
     * @param peaks The peaks to be searched.
     * @param ignoreTolerance Whether tolerance should be ignored or not.
     * @return A list of the potential previous peaks for the given peak.
     **************************************************************************/
    public List<PeakMatch> findPeaks( IPeak peak, List<IPeak> peaks,
        boolean ignoreTolerance )
    {
        return findPeaks( peak, peaks, ignoreTolerance, false );
    }

    /***************************************************************************
     * @param peak
     * @param peaks
     * @param ignoreTolerance
     * @param ignorePreviousAssignments
     * @return
     **************************************************************************/
    public List<PeakMatch> findPeaks( IPeak peak, List<IPeak> peaks,
        boolean ignoreTolerance, boolean ignorePreviousAssignments )
    {
        List<PeakMatch> lastPeaks = new ArrayList<PeakMatch>();
        PreviousPeakMatcher ppe = new PreviousPeakMatcher( peak,
            ignoreTolerance, ignorePreviousAssignments );

        for( IPeak p : peaks )
        {
            if( ppe.matches( p ) )
            {
                DoubleMatch am = ppe.getAlphaMatch();
                DoubleMatch bm = ppe.getBetaMatch();
                PeakMatch newPeak = new PeakMatch( p, am, bm );

                lastPeaks.add( newPeak );
            }
        }

        Collections.sort( lastPeaks, new OverallPeakMatchComparator() );

        return lastPeaks;
    }
}

class OverallPeakMatchComparator implements Comparator<PeakMatch>
{
    @Override
    public int compare( PeakMatch thisPeak, PeakMatch thatPeak )
    {
        Double thisDiff = thisPeak.getOverallDiff();
        Double thatDiff = thatPeak.getOverallDiff();

        if( thisDiff != null && thatDiff != null )
        {
            return Double.compare( thisDiff, thatDiff );
        }
        else if( thisDiff != null )
        {
            return -1;
        }
        else if( thatDiff != null )
        {
            return 1;
        }

        return 0;
    }
}
