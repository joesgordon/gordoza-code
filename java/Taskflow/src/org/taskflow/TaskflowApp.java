package org.taskflow;

import java.awt.*;

import javax.swing.JFrame;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.ui.app.IFrameApp;
import org.tuvok.ui.TaskflowFrameView;
import org.tuvok.ui.TaskflowMenu;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TaskflowApp implements IFrameApp
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame createFrame()
    {
        TaskflowFrameView frameView = new TaskflowFrameView();
        JFrame frame = frameView.getView();
        TaskflowMenu popup = new TaskflowMenu( frameView );
        Image img = IconConstants.getImage( IconConstants.CALENDAR_16 );

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
