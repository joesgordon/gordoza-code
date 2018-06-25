package org.jutils.appgallery;

import java.awt.Image;
import java.util.List;

import org.jutils.io.IconLoader;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class AppGalleryIcons
{
    /**  */
    public static final String APP_16 = "app_16.png";

    /**  */
    public static final IconLoader loader = new IconLoader(
        AppGalleryIcons.class, "icons" );

    /***************************************************************************
     * 
     **************************************************************************/
    private AppGalleryIcons()
    {
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static List<Image> getAppImages()
    {
        return loader.getImages( IconLoader.buildNameList( "app_" ) );
    }

    /***************************************************************************
     * @param name
     * @return
     **************************************************************************/
    public static Image getImage( String name )
    {
        return loader.getImage( name );
    }
}
