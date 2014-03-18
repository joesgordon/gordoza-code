package org.jutils.io;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

//TODO comments

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
     * Returns a human readable representation of bytes where a KB = 1024 bytes.
     * Hat tip SO question <a
     * href="http://stackoverflow.com/questions/3758606">3758606</a>
     * @param count the number of bytes.
     * @return the human readable string.
     **************************************************************************/
    public static String byteCount( long count )
    {
        int unit = 1024;
        int exp;
        char pre;

        if( count < unit )
        {
            return count + " B";
        }

        exp = ( int )( Math.log( count ) / Math.log( unit ) );
        pre = "KMGTPE".charAt( exp - 1 );
        return String.format( "%.1f %ciB", count / Math.pow( unit, exp ), pre );
    }

    /***************************************************************************
     * @param files
     * @return
     **************************************************************************/
    public static File findClosestCommonAncestor( File... files )
    {
        File ans = null;
        String ansPath = null;
        String [] paths = new String[files.length];

        for( int i = 0; i < files.length; i++ )
        {
            paths[i] = files[i].getAbsolutePath();

            paths[i] = paths[i].replace( '\\', '/' );

            if( files[i].isDirectory() )
            {
                paths[i] += '/';
            }
        }

        for( String path : paths )
        {
            if( ansPath == null )
            {
                ansPath = new File( path ).getParent();
                ansPath += '/';
                ansPath = ansPath.replace( '\\', '/' );
            }
            else
            {
                int i = 0;
                for( ; i < ansPath.length() && i < path.length(); i++ )
                {
                    if( ansPath.charAt( i ) != path.charAt( i ) )
                    {
                        break;
                    }
                }

                i = ansPath.lastIndexOf( '/', i );
                if( i > -1 )
                {
                    ansPath = ansPath.substring( 0, i ) + '/';
                }
                else
                {
                    ansPath = null;
                    break;
                }
            }
        }

        if( ansPath != null )
        {
            ans = new File( ansPath );
        }

        return ans;
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
     * @param names
     * @return
     **************************************************************************/
    public static File getUsersFile( String... names )
    {
        if( names.length < 1 )
        {
            throw new IllegalArgumentException(
                "Must specify at least one name." );
        }

        File file = getUsersFile( names[0] );

        for( int i = 1; i < names.length; i++ )
        {
            file = new File( file, names[i] );
        }

        return file;
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

    /***************************************************************************
     * @param directory
     * @return
     **************************************************************************/
    public static List<File> getAllFiles( File dir )
    {
        List<File> files = new ArrayList<File>();

        getAllFiles( dir, files );

        return files;
    }

    /***************************************************************************
     * @param file
     * @param files
     **************************************************************************/
    private static void getAllFiles( File file, List<File> files )
    {
        if( file.isDirectory() )
        {
            File [] fs = file.listFiles();

            if( fs == null )
            {
                return;
            }

            for( File f : fs )
            {
                if( f.isDirectory() )
                {
                    getAllFiles( f, files );
                }
                else
                {
                    files.add( f );
                }
            }
        }
    }

    public static String getFileExtension( File file )
    {
        String fileName = file.getName();
        String extension = "";

        int i = fileName.lastIndexOf( '.' );

        if( i > -1 )
        {
            extension = fileName.substring( i + 1 );
        }

        return extension;
    }
}
