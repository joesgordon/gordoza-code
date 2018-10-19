package org.taskflow;

import java.awt.*;

import javax.swing.JFrame;

import org.jutils.SwingUtils;
import org.jutils.ui.app.IFrameApp;
import org.taskflow.ui.TaskflowFrameView;
import org.taskflow.ui.TaskflowMenu;

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
        Image img = TaskflowIcons.getImage( TaskflowIcons.APP_016 );

        TrayIcon icon = SwingUtils.createTrayIcon( img, "Taskflow", frame,
            null );
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
