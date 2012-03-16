package org.jutils.io;

import java.io.File;

/*******************************************************************************
 *
 ******************************************************************************/
public final class UFile
{
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
            // System.out.println( "\tChecking: " + descendant.getAbsolutePath()
            // );
            descendant = descendant.getParentFile();
            // if( descendant == null )
            // {
            // System.out.println( "\t\tHas no parent!" );
            // }
        }

        return descendant != null;
    }

    /***************************************************************************
     * @param paths
     * @return
     **************************************************************************/
    public static File[] getFilesFromString( String paths )
    {
        String[] dirPaths = paths.split( File.pathSeparator );
        File[] dirs = new File[dirPaths.length];

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
    public static String getStringFromFiles( File[] files )
    {
        String paths = "";
        for( int i = 0; i < files.length; i++ )
        {
            if( paths.length() > 0 )
            {
                paths += File.pathSeparator;
            }
            paths += files[i].getAbsolutePath();
        }
        return paths;
    }
}
