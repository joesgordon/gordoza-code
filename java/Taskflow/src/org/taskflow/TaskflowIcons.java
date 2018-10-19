package org.taskflow;

import java.awt.Image;
import java.util.List;

import javax.swing.ImageIcon;

import org.jutils.io.IconLoader;

/*******************************************************************************
 *
 ******************************************************************************/
public final class TaskflowIcons
{
    /** The loader used to access the icons. */
    public final static IconLoader loader = new IconLoader( TaskflowIcons.class,
        "icons" );

    /**  */
    public static final String APP_016 = "calendar_" + "016.png";;
    /**  */
    public static final String APP_024 = "calendar_" + "024.png";

    /***************************************************************************
     * Private default constructor to prevent instantiation of this class.
     **************************************************************************/
    private TaskflowIcons()
    {
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static List<Image> getAppImages()
    {
        return loader.getImages( IconLoader.buildNameList( "calendar_" ) );
    }

    /***************************************************************************
     * @param str
     * @return
     **************************************************************************/
    public static ImageIcon getIcon( String name )
    {
        return loader.getIcon( name );
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
