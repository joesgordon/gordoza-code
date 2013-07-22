package org.jutils.io;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
        INSTALL_DIR = getInstallDir( IOUtils.class );
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

    /***************************************************************************
     * @param filename
     * @return
     **************************************************************************/
    public static File getUsersFile( String filename )
    {
        return new File( USERS_DIR, filename );
    }

    /***************************************************************************
     * @param filename
     * @return
     **************************************************************************/
    public static File getInstallFile( String filename )
    {
        return new File( INSTALL_DIR, filename );
    }

    /***************************************************************************
     * @param filename
     * @return
     **************************************************************************/
    public static File getInstallFile( String filename, Class<?> installClass )
    {
        File dir = getInstallDir( installClass );

        return new File( dir, filename );
    }

    /***************************************************************************
     * @param file
     * @return
     **************************************************************************/
    public static File removeExtension( File file )
    {
        File parent = file.getAbsoluteFile().getParentFile();

        return new File( parent, removeFilenameExtension( file ) );
    }

    /***************************************************************************
     * @param file
     * @return
     **************************************************************************/
    public static String removeFilenameExtension( File file )
    {
        String filename = file.getName();

        int extensionIndex = filename.lastIndexOf( "." );

        if( extensionIndex == -1 )
        {
            return filename;
        }

        return filename.substring( 0, extensionIndex );
    }

    /***************************************************************************
     * @param installClass
     * @return
     **************************************************************************/
    public static File getInstallDir( Class<?> installClass )
    {
        File file = null;
        URL url = installClass.getProtectionDomain().getCodeSource().getLocation();

        try
        {
            file = new File( url.toURI() ).getParentFile();
        }
        catch( URISyntaxException ex )
        {
            throw new RuntimeException( "Unknown install location: " +
                url.toString(), ex );
        }

        return file;
    }

    /***************************************************************************
     * @param file
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     **************************************************************************/
    public static List<String> readAllLines( File file )
        throws FileNotFoundException, IOException
    {
        List<String> lines = new ArrayList<String>();
        BufferedReader reader = null;

        try
        {
            reader = new BufferedReader( new FileReader( file ) );

            String line;

            while( ( line = reader.readLine() ) != null )
            {
                lines.add( line );
            }
        }
        finally
        {
            if( reader != null )
            {
                reader.close();
            }
        }

        return lines;
    }

    /***************************************************************************
     * @param prefix
     * @param suffix
     * @return
     * @throws IOException
     **************************************************************************/
    public static File createSelfDemisingTempFile( String prefix, String suffix )
        throws IOException
    {
        File f = File.createTempFile( prefix, suffix );

        f.deleteOnExit();

        return f;
    }
}
