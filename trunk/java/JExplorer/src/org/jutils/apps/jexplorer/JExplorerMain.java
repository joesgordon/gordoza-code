package org.jutils.apps.jexplorer;

import java.io.File;

import javax.swing.JFrame;

import org.jutils.ui.app.FrameApplication;
import org.jutils.ui.app.IFrameApp;

/*******************************************************************************
 * This class defines the application that will display the main applications
 * contained in JUtils.
 ******************************************************************************/
public class JExplorerMain implements IFrameApp
{
    /***************************************************************************
     * Create the AppGalley frame.
     * @return
     **************************************************************************/
    public JFrame createFrame()
    {
        JExplorerFrame frame = new JExplorerFrame();
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setDirectory( new File( "/" ) );

        return frame;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void finalizeGui()
    {
    }

    /***************************************************************************
     * Application Gallery definition to display an AppGallery frame.
     * @param args Unused arguments to the Application Gallery application.
     **************************************************************************/
    public static void main( String[] args )
    {
        FrameApplication.invokeLater( new JExplorerMain() );
    }
}
