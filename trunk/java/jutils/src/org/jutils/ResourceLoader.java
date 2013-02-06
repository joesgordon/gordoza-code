package org.jutils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ResourceLoader
{
    /**  */
    private final URL baseUrl;

    /***************************************************************************
     * @param baseClass
     * @param relPath
     **************************************************************************/
    public ResourceLoader( Class<?> baseClass, String relPath )
    {
        this( Utils.loadResourceURL( baseClass,
            relPath.endsWith( "/" ) ? relPath : relPath + "/" ) );
    }

    /***************************************************************************
     * @param basePath
     * @throws MalformedURLException
     **************************************************************************/
    public ResourceLoader( File basePath ) throws MalformedURLException
    {
        this( basePath.toURI().toURL() );
    }

    /***************************************************************************
     * @param url
     **************************************************************************/
    public ResourceLoader( URL url )
    {
        baseUrl = url;
    }

    /***************************************************************************
     * @param name
     * @return
     **************************************************************************/
    public URL getUrl( String name )
    {
        URL url = null;
        try
        {
            url = new URL( baseUrl.toString() + name );
        }
        catch( MalformedURLException ex )
        {
            throw new RuntimeException( ex );
        }

        return url;
    }

    /***************************************************************************
     * @param name
     * @return
     **************************************************************************/
    public InputStream getInputStream( String name )
    {
        InputStream stream = null;

        try
        {
            stream = getUrl( name ).openStream();
        }
        catch( IOException ex )
        {
            throw new RuntimeException( ex );
        }

        return stream;
    }
}
