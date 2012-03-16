package nmrc.ui;

import java.awt.Frame;
import java.util.List;

import nmrc.data.PeakMatch;
import nmrc.model.IPeak;
import nmrc.model.IPeakChooser;
import nmrc.ui.tables.models.PeakMatchTableModel;
import nmrc.ui.tables.models.PeakRecordTableModel;

/***************************************************************************
 * Provides a graphical method of choosing a peak.
 **************************************************************************/
public class PeakMatchChooser implements IPeakChooser
{
    /**  */
    private ChoicesDialog<PeakMatch> choicesDialog;
    /**  */
    private PeakRecordTableModel recordTableModel;
    /**  */
    private PeakMatchTableModel matchTableModel;

    /***************************************************************************
     * @param owner
     **************************************************************************/
    public PeakMatchChooser( Frame owner )
    {
        recordTableModel = new PeakRecordTableModel();
        matchTableModel = new PeakMatchTableModel();

        choicesDialog = new ChoicesDialog<PeakMatch>( owner, "",
            recordTableModel, matchTableModel );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public PeakMatch choosePeak( IPeak peak, List<PeakMatch> peaks )
    {
        recordTableModel.clearItems();
        recordTableModel.addRow( peak );

        matchTableModel.clearItems();
        matchTableModel.setItems( peaks );

        boolean isboth = false;
        if( peaks.size() > 0 )
        {
            choicesDialog.setSelectedRow( 0 );
            PeakMatch p = peaks.get( 0 );

            isboth = p.getAlphaMatch() != null && p.getBetaMatch() != null;
        }

        for( int row = 1; !isboth && row < peaks.size(); row++ )
        {
            PeakMatch p = peaks.get( row );

            isboth = p.getAlphaMatch() != null && p.getBetaMatch() != null;
            if( isboth )
            {
                choicesDialog.setSelectedRow( row );
            }
        }

        choicesDialog.setVisible( true );

        if( choicesDialog.isCancelled() )
        {
            return null;
        }

        return choicesDialog.getChoice();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public boolean isCancelled()
    {
        return choicesDialog.isCancelled();
    }
}
