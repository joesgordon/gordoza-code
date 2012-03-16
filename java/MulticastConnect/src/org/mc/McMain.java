package org.mc;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.jutils.ui.FrameRunner;
import org.mc.ui.McFrame;

/*******************************************************************************
 * 
 ******************************************************************************/
public class McMain extends FrameRunner
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( new McMain() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    protected JFrame createFrame()
    {
        McFrame frame = new McFrame();

        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setSize( 500, 500 );

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
