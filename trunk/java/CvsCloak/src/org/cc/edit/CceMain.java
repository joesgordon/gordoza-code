package org.cc.edit;

import javax.swing.JFrame;

import org.cc.edit.ui.CceFrame;
import org.jutils.ui.app.FrameApplication;
import org.jutils.ui.app.IFrameApp;

/*******************************************************************************
 * This class defines main for CCEdit.
 ******************************************************************************/
public class CceMain implements IFrameApp
{
    @Override
    public JFrame createFrame()
    {
        CceFrame appFrame = new CceFrame();

        appFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        DataMaker dm = new DataMaker();

        appFrame.setData( dm.createModel() );

        return appFrame;
    }

    @Override
    public void finalizeGui()
    {
    }

    /***************************************************************************
     * The main function.
     * @param args Unused arguments.
     **************************************************************************/
    public static void main( String[] args )
    {
        FrameApplication.invokeLater( new CceMain() );
    }
}
