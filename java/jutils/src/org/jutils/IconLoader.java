package org.jutils;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import javax.swing.ImageIcon;

/*******************************************************************************
 * Defines methods of loading icons that reside either in a directory or
 * relative to a class file. Aids the developer in accessing icons in a manner
 * independent of whether the class files are in a directory or a jar file. All
 * icons must be copied to the directory or jar file by the build tool.
 ******************************************************************************/
public class IconLoader
{
    /** The URL at which all the icons reside. */
    public final ResourceLoader loader;
    /**
     * The icon cache so that icons are not loaded more than once per instance
     * of this class.
     */
    private final Map<String, ImageIcon> iconMap;

    /***************************************************************************
     * @param basePath
     * @throws MalformedURLException
     **************************************************************************/
    public IconLoader( File basePath ) throws MalformedURLException
    {
        this( new ResourceLoader( basePath ) );
    }

    /***************************************************************************
     * @param baseClass
     * @param relative
     **************************************************************************/
    public IconLoader( Class<?> baseClass, String relativePath )
    {
        this( new ResourceLoader( baseClass, relativePath ) );
    }

    /***************************************************************************
     * @param url
     **************************************************************************/
    public IconLoader( URL url )
    {
        this( new ResourceLoader( url ) );
    }

    /***************************************************************************
     * @param resourceLoader
     **************************************************************************/
    public IconLoader( ResourceLoader resourceLoader )
    {
        this.loader = resourceLoader;
        iconMap = new HashMap<String, ImageIcon>();
    }

    /***************************************************************************
     * @param str String
     * @return ImageIcon
     **************************************************************************/
    public ImageIcon getIcon( String str )
    {
        ImageIcon icon = null;

        if( iconMap.containsKey( str ) )
        {
            icon = iconMap.get( str );
        }
        else
        {
            System.out.println( "Getting icon: " + str );
            icon = new ImageIcon( loader.getUrl( str ) );
            iconMap.put( str, icon );
        }

        return icon;
    }

    /***************************************************************************
     * @param names
     * @return
     **************************************************************************/
    public List<ImageIcon> getIcons( String... names )
    {
        List<ImageIcon> icons = new ArrayList<ImageIcon>();

        for( String name : names )
        {
            icons.add( getIcon( name ) );
        }

        return icons;
    }

    /***************************************************************************
     * @param str
     * @return
     * @throws IOException
     **************************************************************************/
    public Image getImage( String str )
    {
        return getIcon( str ).getImage();
    }

    /***************************************************************************
     * @param names
     * @return
     **************************************************************************/
    public List<Image> getImages( String... names )
    {
        List<Image> images = new ArrayList<Image>();

        for( String name : names )
        {
            Image img = getImage( name );

            if( img != null )
            {
                images.add( img );
            }
        }

        return images;
    }
}
