package org.tuvok;

import java.awt.Dimension;
import java.awt.Image;

import javax.swing.JFrame;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.ui.app.FrameApplication;
import org.jutils.ui.app.IFrameApp;

/*******************************************************************************
 *
 ******************************************************************************/
public class ToDLsMain implements IFrameApp
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
        FrameApplication.invokeLater( new ToDLsMain() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame createFrame()
    {
        ToDLsFrame frame = new ToDLsFrame();
        ToDLsMenu popup = new ToDLsMenu( frame );
        Image img = IconConstants.loader.getImage( IconConstants.CALENDAR_16 );

        SwingUtils.createTrayIcon( img, "Tuvok", frame, popup );

        frame.setSize( new Dimension( 640, 480 ) );
        frame.setMinimumSize( new Dimension( 300, 300 ) );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        return frame;
    }

    @Override
    public void finalizeGui()
    {
    }
}
