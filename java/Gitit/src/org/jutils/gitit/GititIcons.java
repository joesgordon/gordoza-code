package org.jutils.gitit;

import java.awt.Image;
import java.util.List;

import javax.swing.ImageIcon;

import org.jutils.core.io.IconLoader;

public class GititIcons
{
    /**  */
    public static final IconLoader loader = new IconLoader( GititIcons.class,
        "icons" );

    /**  */
    public static final String APP_024 = "git_024.png";

    /***************************************************************************
     * Private default constructor to prevent instantiation of this class.
     **************************************************************************/
    private GititIcons()
    {
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static List<Image> getAppImages()
    {
        return loader.getImages( IconLoader.buildNameList( "git_" ) );
    }

    /***************************************************************************
     * @param name
     * @return
     **************************************************************************/
    public static ImageIcon getIcon( String name )
    {
        return loader.getIcon( name );
    }
}
