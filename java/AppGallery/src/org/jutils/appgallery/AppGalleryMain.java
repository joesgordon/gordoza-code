package org.jutils.appgallery;

import org.jutils.ui.app.FrameApplication;

/*******************************************************************************
 * This class defines the application that will display the main applications
 * contained in JUtils.
 ******************************************************************************/
public class AppGalleryMain
{
    /***************************************************************************
     * Application Gallery definition to display an AppGallery frame.
     * @param args Unused arguments to the Application Gallery application.
     **************************************************************************/
    public static void main( String[] args )
    {
        FrameApplication.invokeLater( new AppGalleryApp() );
    }
}
