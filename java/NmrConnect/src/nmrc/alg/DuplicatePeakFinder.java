package nmrc.alg;

import java.awt.Component;
import java.awt.Frame;
import java.util.List;

import org.jutils.SwingUtils;

import nmrc.model.IPeak;
import nmrc.ui.RecordChoiceDialog;
import nmrc.ui.tables.models.PeakRecordTableModel;

/*******************************************************************************
 * Class that finds all the duplicate peaks in a given list of peaks.
 ******************************************************************************/
public class DuplicatePeakFinder
{
    /***************************************************************************
     * @param owner
     * @param peaks
     * @return
     **************************************************************************/
    public void findDuplicates( Component owner, List<IPeak> peaks )
    {
        Frame frame = SwingUtils.getComponentsFrame( owner );
        ItemFinder<IPeak> peakFinder = new ItemFinder<IPeak>();

        for( int i = 0; i < peaks.size(); i++ )
        {
            IPeak peak = peaks.get( i );
            DuplicatePeakMatcher dupTester = new DuplicatePeakMatcher( peak );
            List<IPeak> dups = peakFinder.findItems( dupTester, i, peaks );

            if( dups.size() > 0 )
            {
                RecordChoiceDialog dialog = new RecordChoiceDialog( frame, dups,
                    peak, PeakRecordTableModel.COL_NAMES,
                    "Choose the peaks that are the same:" );
                dialog.setVisible( true );
                if( dialog.isCancelled() )
                {
                    break;
                }

                List<IPeak> choices = dialog.getChoices();
                if( choices != null )
                {
                    for( IPeak pk : choices )
                    {
                        peak.getRecord().addDuplicate( pk.getRecord() );
                        peaks.remove( pk );
                    }
                }
            }
        }
    }
}
