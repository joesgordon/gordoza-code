package org.jutils.appgallery;

import java.awt.TrayIcon;
import java.util.List;

import javax.swing.*;

import org.jutils.SwingUtils;
import org.jutils.appgallery.ui.AppGalleryFrameView;
import org.jutils.ui.ExitListener;
import org.jutils.ui.IToolView;
import org.jutils.ui.app.IFrameApp;

/*******************************************************************************
 * Defines the set of functions that displays a frame application.
 ******************************************************************************/
public class AppGalleryApp implements IFrameApp
{
    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JFrame createFrame()
    {
        List<IToolView> apps = AppGalleryMain.getTools();
        AppGalleryFrameView frameView = new AppGalleryFrameView( apps );
        JFrame frame = frameView.getView();

        TrayIcon icon = SwingUtils.createTrayIcon(
            AppGalleryIcons.getImage( AppGalleryIcons.APP_16 ), "Tray Helper",
            frame, null );

        SwingUtils.addTrayMenu( icon, createPopup( frameView ) );

        return frame;
    }

    /***************************************************************************
     * Creates the popup menu for the tray icon that displays all the apps in
     * the gallery.
     * @param frame the frame to use as the parent of the popup menu.
     * @return the popup menu for this application's tray icon.
     **************************************************************************/
    private JPopupMenu createPopup( AppGalleryFrameView frame )
    {
        JPopupMenu menu = new JPopupMenu();

        menu.add( frame.createMenu() );

        menu.addSeparator();

        JMenuItem menuItem = new JMenuItem( "Exit" );
        menuItem.addActionListener( new ExitListener( frame.getView() ) );
        menu.add( menuItem );

        return menu;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void finalizeGui()
    {
    }
}
