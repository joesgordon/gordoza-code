package org.jutils.apps;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.jutils.IconConstants;
import org.jutils.ui.ExitListener;
import org.jutils.ui.FrameRunner;

/*******************************************************************************
 * This class defines the application that will display the main applications
 * contained in JUtils.
 ******************************************************************************/
public class AppGalleryMain extends FrameRunner
{
    private final List<ILibraryApp> apps;

    private AppGalleryMain()
    {
        apps = new ArrayList<ILibraryApp>();

        apps.add( new FileSpyApp() );
        apps.add( new JHexApp() );
        apps.add( new JExplorerApp() );
        apps.add( new BudgeyApp() );
        apps.add( new DuakApp() );
    }

    /***************************************************************************
     * Create the AppGalley frame.
     * @return
     **************************************************************************/
    @Override
    protected JFrame createFrame()
    {
        JFrame frame = new JFrame();
        AppGalleryFrame appFrame = new AppGalleryFrame( apps );

        Image img = IconConstants.getImage( IconConstants.LAUNCH_16 );

        frame.setContentPane( appFrame.getView() );
        frame.setTitle( "JUtils Application Gallery" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        createTrayIcon( img, "App Gallery", frame, createPopupMenu( frame ) );

        return frame;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private PopupMenu createPopupMenu( JFrame frame )
    {
        PopupMenu menu = new PopupMenu();
        MenuItem menuItem;

        for( ILibraryApp app : apps )
        {
            menuItem = new MenuItem( app.getName() );
            menuItem.addActionListener( new AppButtonListener( app ) );
            menu.add( menuItem );
        }

        menu.addSeparator();

        menuItem = new MenuItem( "Exit" );
        menuItem.addActionListener( new ExitListener( frame ) );
        menu.add( menuItem );

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
