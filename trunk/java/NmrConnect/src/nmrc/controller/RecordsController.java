package nmrc.controller;

import java.util.*;

import nmrc.alg.DuplicatePeakFinder;
import nmrc.data.Peak;
import nmrc.model.*;
import nmrc.ui.ChoicesDialog;
import nmrc.ui.panels.RecordsPanel;
import nmrc.ui.tables.models.PeakRecordTableModel;

import org.jutils.Utils;
import org.jutils.ui.event.ItemActionEvent;
import org.jutils.ui.event.ItemActionListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RecordsController
{
    /**  */
    private RecordsPanel recordsPanel;

    /***************************************************************************
     * @param panel
     **************************************************************************/
    public RecordsController( RecordsPanel panel )
    {
        recordsPanel = panel;

        ItemActionListener<INmrData> findDuplicatesListener = new ItemActionListener<INmrData>()
        {
            @Override
            public void actionPerformed( ItemActionEvent<INmrData> event )
            {
                findDuplicates( event.getItem() );
            }
        };

        ItemActionListener<IPeak> removeDuplicatesListener = new ItemActionListener<IPeak>()
        {
            @Override
            public void actionPerformed( ItemActionEvent<IPeak> event )
            {
                removeDuplicates( event.getItem() );
            }
        };

        panel.addFindDuplicatesListener( findDuplicatesListener );
        panel.addAssignDuplicatesListener( new AssignListener() );
        panel.addRemoveDuplicatesListener( removeDuplicatesListener );
    }

    /***************************************************************************
     * @param data
     **************************************************************************/
    private void findDuplicates( INmrData data )
    {
        DuplicatePeakFinder finder = new DuplicatePeakFinder();

        finder.findDuplicates( recordsPanel, data.getPeaks() );

        recordsPanel.setData( data );

        // TODO Make sure AminoAcids are reset. Pop up a dialog box saying all
        // the I'm removing the assigned peak from.
    }

    private class AssignListener implements ItemActionListener<IPeak>
    {
        @Override
        public void actionPerformed( ItemActionEvent<IPeak> event )
        {
            IPeak peak = event.getItem();
            INmrData data = recordsPanel.getData();
            List<IPeak> choices = new ArrayList<IPeak>( data.getPeaks() );

            choices.remove( peak );

            PeakRecordTableModel peakModel = new PeakRecordTableModel();
            PeakRecordTableModel choicesModel = new PeakRecordTableModel();

            peakModel.addRow( peak );
            choicesModel.setItems( choices );

            ChoicesDialog<IPeak> dialog = new ChoicesDialog<IPeak>(
                Utils.getComponentsFrame( recordsPanel ), "", peakModel,
                choicesModel );

            dialog.setVisible( true );

            if( !dialog.isCancelled() )
            {
                IPeak choice = dialog.getChoice();
                if( choice != null )
                {
                    peak.getRecord().addDuplicate( choice.getRecord() );

                    data.getPeaks().remove( choice );

                    recordsPanel.setData( data );
                }
            }
        }
    }

    /***************************************************************************
     * @param item
     **************************************************************************/
    private void removeDuplicates( IPeak peak )
    {
        INmrData data = recordsPanel.getData();
        List<IPeakRecord> records = new ArrayList<IPeakRecord>(
            peak.getRecord().getDuplicates() );

        for( IPeakRecord record : records )
        {
            peak.getRecord().removeDuplicate( record );

            IPeak newPeak = new Peak( record );
            data.getPeaks().add( newPeak );
        }

        Collections.sort( data.getPeaks(), new PeakIndexComparator() );

        recordsPanel.setData( data );
    }
}

class PeakIndexComparator implements Comparator<IPeak>
{
    @Override
    public int compare( IPeak thisPeak, IPeak thatPeak )
    {
        int diff = thisPeak.getRecord().getIndex() -
            thatPeak.getRecord().getIndex();

        return diff;
    }
}
