package org.cc.view;

import javax.swing.JFrame;

import org.cc.view.ui.ClearViewFrame;
import org.jutils.ui.app.FrameApplication;
import org.jutils.ui.app.IFrameApp;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ClearViewMain implements IFrameApp
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String[] args )
    {
        FrameApplication.invokeLater( new ClearViewMain() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame createFrame()
    {
        ClearViewFrame frame = new ClearViewFrame();

        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setSize( 500, 400 );

        return frame;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void finalizeGui()
    {
    }
}
