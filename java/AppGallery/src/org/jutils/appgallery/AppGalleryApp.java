package org.jutils.appgallery;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.appgallery.apps.*;
import org.jutils.appgallery.ui.AppGalleryView;
import org.jutils.ui.ExitListener;
import org.jutils.ui.StandardFrameView;
import org.jutils.ui.app.IFrameApp;

/*******************************************************************************
 * 
 ******************************************************************************/
public class AppGalleryApp implements IFrameApp
{
    /**  */
    private final List<ILibraryApp> apps;

    /***************************************************************************
     * 
     **************************************************************************/
    public AppGalleryApp()
    {
        apps = new ArrayList<ILibraryApp>();

        apps.add( new FileSpyApp() );
        apps.add( new JHexApp() );
        apps.add( new JExplorerApp() );
        apps.add( new ChatterboxApp() );
        apps.add( new BudgeyApp() );
        apps.add( new DuakApp() );
        apps.add( new SheetApp() );
        apps.add( new ChartApp() );
        apps.add( new TaskflowLibApp() );
    }

    /***************************************************************************
     * Create the AppGalley frame.
     * @return
     **************************************************************************/
    @Override
    public JFrame createFrame()
    {
        StandardFrameView frameView = new StandardFrameView();
        AppGalleryView appFrame = new AppGalleryView( apps );
        JFrame frame = frameView.getView();

        Image img = IconConstants.getImage( IconConstants.LAUNCH_16 );

        frameView.setContent( appFrame.getView() );

        frame.setIconImages( AppGalleryIcons.getAppImages() );
        frame.setTitle( "JUtils Application Gallery" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setSize( 500, 500 );

        SwingUtils.createTrayIcon( img, "App Gallery", frame,
            createPopupMenu( frame ) );

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
    public void finalizeGui()
    {
    }
}
