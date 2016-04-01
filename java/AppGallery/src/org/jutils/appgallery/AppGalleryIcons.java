package org.jutils.appgallery;

import java.awt.Image;
import java.util.List;

import org.jutils.IconLoader;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class AppGalleryIcons
{
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
}
