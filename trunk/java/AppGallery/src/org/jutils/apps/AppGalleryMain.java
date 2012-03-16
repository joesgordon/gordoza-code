package org.jutils.apps;

import java.awt.*;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.jutils.IconConstants;
import org.jutils.ui.FrameRunner;

/*******************************************************************************
 * This class defines the application that will display the main applications
 * contained in JUtils.
 ******************************************************************************/
public class AppGalleryMain extends FrameRunner
{
    /***************************************************************************
     * Create the AppGalley frame.
     * @return
     **************************************************************************/
    @Override
    protected JFrame createFrame()
    {
        AppGalleryFrame appFrame = new AppGalleryFrame();

        Image img = IconConstants.getImage( IconConstants.LAUNCH_16 );

        createTrayIcon( img, "Tuvok", appFrame, createPopupMenu( appFrame ) );

        appFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        return appFrame;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private PopupMenu createPopupMenu( AppGalleryFrame frame )
    {
        PopupMenu menu = new PopupMenu();

        MenuItem filespyItem = new MenuItem( "FileSpy" );
        MenuItem exitItem = new MenuItem( "Exit" );

        menu.add( filespyItem );
        menu.addSeparator();
        menu.add( exitItem );

        exitItem.addActionListener( frame.new ExitListener() );

        return menu;
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
        SwingUtilities.invokeLater( new AppGalleryMain() );
    }

}
