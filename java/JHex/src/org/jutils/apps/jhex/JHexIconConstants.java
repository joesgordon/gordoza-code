package org.jutils.apps.jhex;

import java.awt.Image;
import java.util.List;

import javax.swing.Icon;

import org.jutils.IconLoader;

/*******************************************************************************
 * 
 ******************************************************************************/
public class JHexIconConstants
{
    /**  */
    public static final IconLoader loader = new IconLoader(
        JHexIconConstants.class, "icons" );
    /**  */
    public static final String INCH_LEFT = "arrow-left.png";
    /**  */
    public static final String JUMP_LEFT = "arrow-left-double.png";

    /**  */
    public static final String INCH_RIGHT = "arrow-right.png";
    /**  */
    public static final String JUMP_RIGHT = "arrow-right-double.png";

    /**  */
    public static final String SHOW_DATA = "flashlight.png";

    /**  */
    public static final String GOTO = "goto.png";

    /**  */
    public static final String APP_016 = "app_016.png";

    /**  */
    public static final String APP_024 = "app_024.png";

    /***************************************************************************
     * @return
     **************************************************************************/
    public static List<Image> getAppImages()
    {
        return loader.getImages( IconLoader.buildNameList( "app_" ) );
    }

    /***************************************************************************
     * @param str
     * @return
     **************************************************************************/
    public static Icon getIcon( String str )
    {
        return loader.getIcon( str );
    }
}
