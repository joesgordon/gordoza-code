package org.jutils.appgallery;

import java.awt.Image;
import java.util.List;

import org.jutils.core.io.IconLoader;

/*******************************************************************************
 * Defines methods of returning the icons for this application.
 ******************************************************************************/
public final class AppGalleryIcons
{
    /** The name of the 16 x 16 pixel icon for this application. */
    public static final String APP_16 = "app_016.png";

    /** The loader used to access icons in this application. */
    public static final IconLoader loader = new IconLoader(
        AppGalleryIcons.class, "icons" );

    /***************************************************************************
     * Private default constructor to prevent instantiation of this class.
     **************************************************************************/
    private AppGalleryIcons()
    {
    }

    /***************************************************************************
     * Loads all the different sized icons that represent this application.
     * @return the icons for this application.
     **************************************************************************/
    public static List<Image> getAppImages()
    {
        return loader.getImages( IconLoader.buildNameList( "app_" ) );
    }

    /***************************************************************************
     * Loads the image with the provided name.
     * @param name the name of the image to be loaded.
     * @return the image of the provided name.
     **************************************************************************/
    public static Image getImage( String name )
    {
        return loader.getImage( name );
    }
}
