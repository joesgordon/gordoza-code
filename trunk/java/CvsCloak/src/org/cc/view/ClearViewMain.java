package org.cc.view;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.cc.view.ui.ClearViewFrame;
import org.jutils.ui.FrameRunner;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ClearViewMain extends FrameRunner
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( new ClearViewMain() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    protected JFrame createFrame()
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
    protected boolean validate()
    {
        return true;
    }
}
