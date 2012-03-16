package org.cc.edit;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;


import org.cc.edit.ui.CceFrame;
import org.jutils.ui.MainRunner;

/*******************************************************************************
 * This class defines main for CCEdit.
 ******************************************************************************/
public class CceMain extends MainRunner
{
    /***************************************************************************
     * Create and show the frame.
     * @return
     **************************************************************************/
    protected void createAndShowGui()
    {
        CceFrame appFrame = new CceFrame();

        // ---------------------------------------------------------------------
        // Validate frames that have preset sizes. Pack frames that have
        // useful preferred size info, e.g. from their layout.
        // ---------------------------------------------------------------------
        appFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        appFrame.validate();
        appFrame.setLocationRelativeTo( null );
        appFrame.setVisible( true );

        DataMaker dm = new DataMaker();

        appFrame.setData( dm.createModel() );
    }

    /***************************************************************************
     * The main function.
     * @param args Unused arguments.
     **************************************************************************/
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( new CceMain() );
    }
}
