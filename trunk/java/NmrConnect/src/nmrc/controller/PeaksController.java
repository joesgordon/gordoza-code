package nmrc.controller;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import nmrc.alg.PreviousPeakFinder;
import nmrc.alg.PreviousPeakSorter;
import nmrc.data.PeakMatch;
import nmrc.model.IPeak;
import nmrc.model.IPeakChooser;
import nmrc.ui.PeakMatchChooser;
import nmrc.ui.panels.PeaksPanel;

import org.jutils.Utils;
import org.jutils.ui.event.ItemActionEvent;
import org.jutils.ui.event.ItemActionListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PeaksController
{
    /**  */
    private PeaksPanel peaksPanel;

    /***************************************************************************
     * @param panel
     **************************************************************************/
    public PeaksController( PeaksPanel panel )
    {
        peaksPanel = panel;

        panel.addAssignListener( new AssignListener() );
        panel.addUnassignListener( new UnassignListener() );
        panel.addSortPeaksListener( new SortListener() );
        panel.addFindPreviousListener( new FindListener() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void findAllPreviousPeaks()
    {
        Frame frame = Utils.getComponentsFrame( peaksPanel );
        PreviousPeakFinder finder = new PreviousPeakFinder();

        IPeakChooser chooser = new PeakMatchChooser( frame );

        PreviousPeakSorter sorter = new PreviousPeakSorter( chooser, finder );
        sorter.sortPeaks( peaksPanel.getData() );

        peaksPanel.setData( peaksPanel.getData() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void findPreviousPeak( boolean ignorePreviousAssignments )
    {
        List<PeakMatch> matches;
        Frame frame = Utils.getComponentsFrame( peaksPanel );
        IPeak selectedPeak = peaksPanel.getSelectedPeak();

        if( selectedPeak != null )
        {
            PreviousPeakFinder peakFinder = new PreviousPeakFinder();

            IPeakChooser chooser = new PeakMatchChooser( frame );

            List<IPeak> peaks = peaksPanel.getData();

            matches = peakFinder.findPeaks( selectedPeak, peaks, true,
                ignorePreviousAssignments );

            PeakMatch match = chooser.choosePeak( selectedPeak, matches );

            if( !chooser.isCancelled() )
            {
                int prevIdx = peaks.indexOf( match.getPeak() );
                IPeak prevPeak = peaks.get( prevIdx );

                if( prevPeak.isPrevious() )
                {
                    // TODO don't return.
                    return;
                }

                selectedPeak.setPrevious( prevPeak, match.getAlpha(),
                    match.getBeta(), match.getPreviousAlpha(),
                    match.getPreviousBeta() );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class AssignListener implements ItemActionListener<IPeak>
    {
        @Override
        public void actionPerformed( ItemActionEvent<IPeak> event )
        {
            findPreviousPeak( false );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class UnassignListener implements ItemActionListener<IPeak>
    {
        @Override
        public void actionPerformed( ItemActionEvent<IPeak> event )
        {
            IPeak peak = event.getItem();
            peak.setPrevious( null, null, null, null, null );
            peaksPanel.setData( peaksPanel.getData() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class SortListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent event )
        {
            findAllPreviousPeaks();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class FindListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent event )
        {
            findPreviousPeak( true );
        }
    }
}
