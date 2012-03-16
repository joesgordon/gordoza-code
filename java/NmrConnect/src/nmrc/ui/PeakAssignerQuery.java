package nmrc.ui;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import nmrc.model.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PeakAssignerQuery implements IPeakAssignerQuery
{
    /**  */
    private Frame parent;

    /***************************************************************************
     * @param parent
     **************************************************************************/
    public PeakAssignerQuery( Frame parent )
    {
        this.parent = parent;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IAssignmentOption chooseOption( List<IAminoAcid> aAcids,
        List<IPeak> chain, List<IAssignmentOption> options )
    {
        PeakAssignerDialog dialog = new PeakAssignerDialog( aAcids, chain,
            options, parent );

        dialog.setVisible( true );

        return dialog.getChoice();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IAssignmentOption confirmOption( List<IAminoAcid> aAcids,
        List<IPeak> chain, IAssignmentOption option )
    {
        List<IAssignmentOption> options = new ArrayList<IAssignmentOption>();
        options.add( option );

        PeakAssignerDialog dialog = new PeakAssignerDialog( aAcids, chain,
            options, parent );

        dialog.setVisible( true );

        return dialog.getChoice();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void displayError( String message )
    {
        JOptionPane.showMessageDialog( parent, message, "ERROR",
            JOptionPane.ERROR_MESSAGE );
    }
}
