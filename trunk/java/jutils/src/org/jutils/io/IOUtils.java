package org.jutils.io;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

/*******************************************************************************
 *
 ******************************************************************************/
public final class IOUtils
{
    /**  */
    public static final File USERS_DIR = new File(
        System.getProperty( "user.home" ) );
    /**  */
    public static final File INSTALL_DIR;

    static
    {
        URL url = IOUtils.class.getProtectionDomain().getCodeSource().getLocation();
        try
        {
            INSTALL_DIR = new File( url.toURI() ).getParentFile();
        }
        catch( URISyntaxException ex )
        {
            throw new RuntimeException( "Unknown install location: " +
                url.toString(), ex );
        }
    }

    /***************************************************************************
     *
     **************************************************************************/
    private IOUtils()
    {
    }

    /***************************************************************************
     * Returns the first directory that exists, starting at the given path and
     * traversing upwards.
     * @param dirPath The path to start looking.
     * @return The first existing directory or parent directory from the given
     * path or {@code null} if the argument is {@code null}.
     **************************************************************************/
    public static File getExistingDir( String dirPath )
    {
        File f = null;

        if( dirPath != null )
        {
            f = new File( dirPath );
            while( !f.isDirectory() )
            {
                f = f.getParentFile();
            }
        }

        return f;
    }

    public static File getUsersFile( String filename )
    {
        return new File( USERS_DIR, filename );
    }

    public static File getInstallFile( String filename )
    {
        return new File( INSTALL_DIR, filename );
    }
}
