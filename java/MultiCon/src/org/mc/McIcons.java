package org.mc;

import java.awt.Image;
import java.util.List;

import javax.swing.ImageIcon;

import org.jutils.io.IconLoader;

/*******************************************************************************
 * 
 ******************************************************************************/
public class McIcons
{
    /**  */
    public static final String MULTICON_016 = "multicon016.png";
    /**  */
    public static final String MULTICON_024 = "multicon024.png";

    /** The loader used to access the icons. */
    public final static IconLoader loader = new IconLoader( McIcons.class,
        "icons" );

    /***************************************************************************
     * Private default constructor to prevent instantiation of this class.
     **************************************************************************/
    private McIcons()
    {
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static List<Image> getMulticonImages()
    {
        return loader.getImages( IconLoader.buildNameList( "multicon" ) );
    }

    /***************************************************************************
     * @param str
     * @return
     **************************************************************************/
    public static ImageIcon getIcon( String str )
    {
        return loader.getIcon( str );
    }
}
