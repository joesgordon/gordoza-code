package org.jutils.appgallery;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.jutils.appgallery.ui.AppGalleryView;

/*******************************************************************************
 * ActionListener that runs the provided application upon invocation.
 ******************************************************************************/
public class AppButtonListener implements ActionListener
{
    private final ILibraryApp app;

    /***************************************************************************
     * Creates a new listener using the provided application.
     **************************************************************************/
    public AppButtonListener( ILibraryApp app )
    {
        this.app = app;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void actionPerformed( ActionEvent e )
    {
        AppGalleryView.displayApp( app );
    }
}
