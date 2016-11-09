package org.tuvok;

import java.awt.*;

import javax.swing.JFrame;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.ui.app.IFrameApp;
import org.tuvok.ui.TuvokFrameView;
import org.tuvok.ui.TuvokMenu;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TuvokApp implements IFrameApp
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame createFrame()
    {
        TuvokFrameView frameView = new TuvokFrameView();
        JFrame frame = frameView.getView();
        TuvokMenu popup = new TuvokMenu( frameView );
        Image img = IconConstants.loader.getImage( IconConstants.CALENDAR_16 );

        TrayIcon icon = SwingUtils.createTrayIcon( img, "Tuvok", frame, null );
        SwingUtils.addTrayMenu( icon, popup.getView() );

        frame.setSize( new Dimension( 640, 480 ) );
        frame.setMinimumSize( new Dimension( 300, 300 ) );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

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
