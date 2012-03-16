package nmrc.alg;

import java.util.List;

import nmrc.data.PeakMatch;
import nmrc.model.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PreviousPeakSorter
{
    /**  */
    private IPeakChooser peakChooser;
    /**  */
    private PreviousPeakFinder previousFinder;

    /***************************************************************************
     * @param chooser
     **************************************************************************/
    public PreviousPeakSorter( IPeakChooser chooser, PreviousPeakFinder finder )
    {
        peakChooser = chooser;
        previousFinder = finder;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void sortPeaks( List<IPeak> peaks )
    {
        boolean foundOne = true;

        while( foundOne )
        {
            foundOne = false;
            for( IPeak peak : peaks )
            {
                if( !peak.hasPrevious() )
                {
                    List<PeakMatch> prevPeaks = previousFinder.findPeaks( peak,
                        peaks );

                    if( prevPeaks.size() == 1 )
                    {
                        PeakMatch match = prevPeaks.get( 0 );
                        IPeak selectedPeak = match.getPeak();
                        int prevIdx = peaks.indexOf( selectedPeak );
                        IPeak prevPeak = peaks.get( prevIdx );

                        setPrevious( peak, prevPeak, match );

                        foundOne = true;
                    }
                }
            }
        }

        for( IPeak peak : peaks )
        {
            if( !peak.hasPrevious() )
            {
                List<PeakMatch> prevPeaks = previousFinder.findPeaks( peak,
                    peaks );
                if( prevPeaks.size() == 1 )
                {
                    PeakMatch match = prevPeaks.get( 0 );
                    int prevIdx = peaks.indexOf( match.getPeak() );
                    IPeak prevPeak = peaks.get( prevIdx );

                    setPrevious( peak, prevPeak, match );
                }
                else if( prevPeaks.size() > 1 )
                {
                    displayChooser( peakChooser, peaks, peak, prevPeaks );
                }
                else
                {
                    prevPeaks = previousFinder.findPeaks( peak, peaks, true );

                    displayChooser( peakChooser, peaks, peak, prevPeaks );
                }
            }
        }
    }

    private void setPrevious( IPeak peak, IPeak previous, IPeakMatch match )
    {
        peak.setPrevious( previous, match.getAlpha(), match.getBeta(),
            match.getPreviousAlpha(), match.getPreviousBeta() );
    }

    private void displayChooser( IPeakChooser peakChooser, List<IPeak> peaks,
        IPeak peak, List<PeakMatch> prevPeaks )
    {
        IPeakMatch match = peakChooser.choosePeak( peak, prevPeaks );

        if( match != null )
        {
            int prevIdx = peaks.indexOf( match.getPeak() );
            IPeak prevPeak = peaks.get( prevIdx );

            setPrevious( peak, prevPeak, match );
        }
        else
        {
            System.out.println( "Peak " + peak.getRecord().getPeakName() +
                " has no previous peak." );
        }
    }
}
