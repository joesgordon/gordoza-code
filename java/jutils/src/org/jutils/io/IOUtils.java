package org.jutils.io;

import java.awt.Component;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.*;

import javax.swing.JOptionPane;

import org.jutils.Utils;
import org.jutils.ValidationException;

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
        return findClosestCommonAncestor( Arrays.asList( files ) );
    }

    /***************************************************************************
     * @param files
     * @return
     **************************************************************************/
    public static File findClosestCommonAncestor( List<File> files )
    {
        File ans = null;
        String ansPath = null;
        String [] paths = new String[files.size()];

        for( int i = 0; i < files.size(); i++ )
        {
            File file = files.get( i );
            paths[i] = file.getAbsolutePath();

            paths[i] = paths[i].replace( '\\', '/' );

            if( file.isDirectory() )
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
                List<String> ansParts = Utils.split( ansPath, '/' );
                List<String> pathParts = Utils.split( path, '/' );

                ansPath = "";

                for( int i = 0; i < ansParts.size() &&
                    i < pathParts.size(); i++ )
                {
                    if( ansParts.get( i ).equals( pathParts.get( i ) ) )
                    {
                        ansPath += ansParts.get( i ) + "/";
                    }
                    else
                    {
                        break;
                    }
                }

                if( ansPath.isEmpty() )
                {
                    ansParts = null;
                    break;
                }

                // int idx = findFirstDiff( ansPath, path );
                //
                // if( idx > 0 )
                // {
                // idx = ansPath.lastIndexOf( '/', idx );
                // ansPath = ansPath.substring( 0, idx ) + '/';
                // }
                // else
                // {
                // ansPath = null;
                // break;
                // }
            }
        }

        if( ansPath != null )
        {
            ans = new File( ansPath );
        }

        return ans;
    }

    public static String getStandardAbsPath( File file )
    {
        String path = file.getAbsolutePath().replace( '\\', '/' );

        if( file.isDirectory() && !path.endsWith( "/" ) )
        {
            path += '/';
        }

        return path;
    }

    /***************************************************************************
     * @param str1
     * @param str2
     * @return
     **************************************************************************/
    public static int findFirstDiff( String str1, String str2 )
    {
        int idx = 0;

        for( ; idx < str1.length() && idx < str2.length(); idx++ )
        {
            if( str1.charAt( idx ) != str2.charAt( idx ) )
            {
                break;
            }
        }

        return idx;
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

    public static long getTotalSize( List<File> files )
    {
        long size = 0;

        for( File f : files )
        {
            size += f.length();
        }

        return size;
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
     * @param ext
     * @return
     **************************************************************************/
    public static File replaceExtension( File file, String ext )
    {
        File parent = file.getAbsoluteFile().getParentFile();
        String name = removeFilenameExtension( file ) + "." + ext;
        File f = new File( parent, name );

        return f;
    }

    /***************************************************************************
     * Returns the file name minus the extension. Does not include path
     * information.
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
            throw new RuntimeException(
                "Unknown install location: " + url.toString(), ex );
        }

        return file;
    }

    /***************************************************************************
     * @param file
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     **************************************************************************/
    public static String readAll( File file )
        throws FileNotFoundException, IOException
    {
        StringBuilder lines = new StringBuilder();

        try( FileReader fr = new FileReader( file );
             BufferedReader reader = new BufferedReader( fr ) )
        {
            String line;

            while( ( line = reader.readLine() ) != null )
            {
                lines.append( line );
                lines.append( Utils.NEW_LINE );
            }
        }

        return lines.toString();
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

        try( FileReader fr = new FileReader( file );
             BufferedReader reader = new BufferedReader( fr ) )
        {
            String line;

            while( ( line = reader.readLine() ) != null )
            {
                lines.add( line );
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
    public static File createSelfDemisingTempFile( String prefix,
        String suffix ) throws IOException
    {
        File f = File.createTempFile( prefix, suffix );

        f.deleteOnExit();

        return f;
    }

    /***************************************************************************
     * Returns a list of all files (in which {@code isDirectory() == false})
     * found in the provided directory.
     * @param directory the directory to be searched.
     * @return the list of all files found in the provided directory.
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

    /***************************************************************************
     * @param file
     * @return
     **************************************************************************/
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

    /***************************************************************************
     * @param file
     * @return
     **************************************************************************/
    public static List<File> getAncestors( File file )
    {
        List<File> files = new ArrayList<>();

        file = file.getAbsoluteFile();

        while( file != null )
        {
            files.add( 0, file );
            file = file.getParentFile();
        }

        return files;
    }

    /***************************************************************************
     * @param dir
     * @throws IOException
     **************************************************************************/
    public static void removeContents( File dir ) throws IOException
    {
        File [] files = dir.listFiles();

        if( files != null )
        {
            for( File f : files )
            {
                if( f.isDirectory() )
                {
                    removeContents( f );
                }

                Files.delete( f.toPath() );
            }
        }
    }

    /***************************************************************************
     * @param file
     * @param name
     * @throws ValidationException
     **************************************************************************/
    public static void validateFileInput( File file, String name )
        throws ValidationException
    {
        if( !file.exists() )
        {
            throw new ValidationException( "The specified " + name +
                " file does not exist: " + file.getName() );
        }
        else if( !file.isFile() )
        {
            throw new ValidationException( "The specified " + name +
                " file exists, but is not a file: " + file.getName() );
        }
        else if( !file.canRead() )
        {
            throw new ValidationException( "Cannot read from the specified " +
                name + " file: " + file.getName() );
        }
    }

    /***************************************************************************
     * @param dir
     * @param name
     * @throws ValidationException
     **************************************************************************/
    public static void validateDirInput( File dir, String name )
        throws ValidationException
    {
        if( !dir.exists() )
        {
            throw new ValidationException( "The specified " + name +
                " directory does not exist: " + dir.getName() );
        }
        else if( !dir.isDirectory() )
        {
            throw new ValidationException( "The specified " + name +
                " directory exists, but is not a directory: " + dir.getName() );
        }
        else if( !dir.canRead() )
        {
            throw new ValidationException( "Cannot read from the specified " +
                name + " directory: " + dir.getName() );
        }
    }

    /***************************************************************************
     * @param dir
     * @param name
     * @throws ValidationException
     **************************************************************************/
    public static void validateExistsInput( File dir, String name )
        throws ValidationException
    {
        if( !dir.exists() )
        {
            throw new ValidationException( "The specified " + name +
                " path does not exist: " + dir.getName() );
        }
    }

    /***************************************************************************
     * @param outputFile
     * @param string
     * @throws ValidationException
     **************************************************************************/
    public static void validateFileOuput( File file, String name )
        throws ValidationException
    {
        if( !file.exists() )
        {
            File parent = file.getAbsoluteFile().getParentFile();
            if( !parent.exists() )
            {
                throw new ValidationException( "The specified " + name +
                    " file's parent directory does not exist: " +
                    parent.getName() );
            }
            else if( !parent.canWrite() )
            {
                throw new ValidationException(
                    "Cannot write to the specified " + name +
                        " file's parent directory: " + parent.getName() );
            }
        }
        else if( !file.isFile() )
        {
            throw new ValidationException( "The specified " + name +
                " file is not a file: " + file.getName() );
        }
        else if( !file.canWrite() )
        {
            throw new ValidationException( "Cannot write to the specified " +
                name + " file: " + file.getName() );
        }
    }

    /***************************************************************************
     * @param dir
     * @param name
     * @throws ValidationException
     **************************************************************************/
    public static void validateDirOuput( File dir, String name )
        throws ValidationException
    {
        if( !dir.exists() )
        {
            throw new ValidationException( "The specified " + name +
                " directory does not exist: " + dir.getName() );
        }
        else if( !dir.isDirectory() )
        {
            throw new ValidationException( "The specified " + name +
                " directory directory is not a directory: " + dir.getName() );
        }
        else if( !dir.canWrite() )
        {
            throw new ValidationException( "Cannot write to the specified " +
                name + " directory: " + dir.getName() );
        }
    }

    /***************************************************************************
     * @param file
     * @return
     **************************************************************************/
    public static boolean ensureParentExists( File file )
    {
        File dir = file.getAbsoluteFile().getParentFile();

        if( !dir.isDirectory() )
        {
            if( !dir.mkdirs() )
            {
                return false;
            }
        }

        return true;
    }

    /***************************************************************************
     * @param ancestor
     * @param descendant
     * @return
     **************************************************************************/
    public static boolean isAncestorOf( File ancestor, File descendant )
    {
        if( ancestor.equals( descendant ) )
        {
            return false;
        }

        while( ( descendant != null ) && !ancestor.equals( descendant ) )
        {
            // LogUtils.printDebug( "\tChecking: " +
            // descendant.getAbsolutePath() );
            descendant = descendant.getParentFile();
            // if( descendant == null )
            // {
            // LogUtils.printDebug( "\t\tHas no parent!" );
            // }
        }

        return descendant != null;
    }

    /***************************************************************************
     * @param paths
     * @return
     **************************************************************************/
    public static File [] getFilesFromString( String paths )
    {
        String [] dirPaths = paths.split( File.pathSeparator );
        File [] dirs = new File[dirPaths.length];

        for( int i = 0; i < dirs.length; i++ )
        {
            dirs[i] = new File( dirPaths[i] );
        }

        return dirs;
    }

    /***************************************************************************
     * @param files
     * @return
     **************************************************************************/
    public static String getStringFromFiles( File [] files )
    {
        String paths = null;

        if( files != null )
        {
            paths = "";

            for( int i = 0; i < files.length; i++ )
            {
                if( paths.length() > 0 )
                {
                    paths += File.pathSeparator;
                }
                paths += files[i].getAbsolutePath();
            }
        }

        return paths;
    }

    /***************************************************************************
     * @param dir
     * @param ext
     * @return
     **************************************************************************/
    public static List<File> getAllFiles( File dir, String ext )
    {
        FileFilter filter = new ExtensionFilter( ext );
        File [] fa = dir.listFiles( filter );
        List<File> files = new ArrayList<>( fa.length );

        Collections.addAll( files, fa );

        return files;
    }

    /***************************************************************************
     * Checks for contents in the provided directory and asks the user if the
     * contents should be deleted
     * @param dir
     * @param parent
     * @return
     **************************************************************************/
    public static boolean checkForContents( File dir, Component parent )
    {
        File outDir = dir;
        File [] files = outDir.listFiles();

        if( files == null )
        {
            JOptionPane.showMessageDialog( parent,
                "Output directory does not exist or cannot be read: " +
                    Utils.NEW_LINE + outDir.getAbsolutePath(),
                "Configuration Error", JOptionPane.ERROR_MESSAGE );

            return false;
        }
        else if( files.length == 0 )
        {
            return true;
        }

        String delOption = "Delete";
        String contOption = "Continue";
        String cancelOption = "Cancel";
        String [] options = new String[] { delOption, contOption,
            cancelOption };
        int ans = JOptionPane.showOptionDialog( parent,
            "The output directory contains files. Do you want to delete them before proceeding?",
            "Output Directory Not Empty", JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE, null, options, delOption );

        if( ans == JOptionPane.CLOSED_OPTION ||
            ans == JOptionPane.CANCEL_OPTION )
        {
            return false;
        }
        else if( ans == JOptionPane.YES_OPTION )
        {
            try
            {
                IOUtils.removeContents( outDir );
                return true;
            }
            catch( IOException ex )
            {
                JOptionPane.showMessageDialog( parent,
                    "Unable to delete output directory contents: " +
                        ex.getMessage(),
                    "Deletion Error", JOptionPane.ERROR_MESSAGE );
                return false;
            }
        }
        else if( ans == JOptionPane.NO_OPTION )
        {
            return true;
        }

        throw new IllegalStateException(
            "Incorrect handling of call to JOptionPane.showOptionDialog" );
    }

    /***************************************************************************
     * @param dir
     * @param filePattern
     * @return
     * @throws IOException
     **************************************************************************/
    public static List<File> listWithWildcard( File dir, String filePattern )
        throws IOException
    {
        List<File> files = new ArrayList<>();

        try( DirectoryStream<Path> paths = Files.newDirectoryStream(
            dir.toPath(), filePattern ) )
        {
            for( Path p : paths )
            {
                files.add( p.toFile() );
            }
        }

        return files;
    }
}
