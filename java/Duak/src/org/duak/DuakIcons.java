package org.duak;

import java.awt.Image;
import java.util.List;

import org.jutils.IconLoader;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class DuakIcons
{
    /**  */
    public static final IconLoader loader = new IconLoader( DuakIcons.class,
        "icons" );

    /**  */
    public static final String APP_032 = "duak_032.png";

    /***************************************************************************
     * 
     **************************************************************************/
    private DuakIcons()
    {
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static List<Image> getAppImages()
    {
        return loader.getImages( IconLoader.buildImageList( "duak_" ) );
    }
}
