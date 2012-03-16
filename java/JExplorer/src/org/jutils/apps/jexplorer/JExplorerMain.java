package org.jutils.apps.jexplorer;

import java.io.File;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.jutils.ui.FrameRunner;

/*******************************************************************************
 * This class defines the application that will display the main applications
 * contained in JUtils.
 ******************************************************************************/
public class JExplorerMain extends FrameRunner
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
    protected boolean validate()
    {
        return false;
    }

    /***************************************************************************
     * Application Gallery definition to display an AppGallery frame.
     * @param args Unused arguments to the Application Gallery application.
     **************************************************************************/
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( new JExplorerMain() );
    }

}
