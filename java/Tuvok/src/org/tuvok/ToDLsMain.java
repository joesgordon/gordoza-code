package org.tuvok;

import java.awt.Dimension;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.jutils.IconConstants;
import org.jutils.ui.FrameRunner;

/*******************************************************************************
 *
 ******************************************************************************/
public class ToDLsMain extends FrameRunner
{
    /***************************************************************************
     *
     **************************************************************************/
    public ToDLsMain()
    {
    }

    /***************************************************************************
     * @param args String[]
     **************************************************************************/
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( new ToDLsMain() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    protected JFrame createFrame()
    {
        ToDLsFrame frame = new ToDLsFrame();
        ToDLsMenu popup = new ToDLsMenu( frame );
        Image img = IconConstants.getImage( IconConstants.CALENDAR_16 );

        createTrayIcon( img, "Tuvok", frame, popup );

        frame.setSize( new Dimension( 640, 480 ) );
        frame.setMinimumSize( new Dimension( 300, 300 ) );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        return frame;
    }

    @Override
    protected boolean validate()
    {
        return true;
    }
}
