package org.jutils.apps.jhex.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jutils.utils.MaxQueue;

/*******************************************************************************
 * 
 ******************************************************************************/
public class JHexOptions
{
    /**  */
    public final MaxQueue<File> lastAccessedFiles;

    /***************************************************************************
     * 
     **************************************************************************/
    public JHexOptions()
    {
        this.lastAccessedFiles = new MaxQueue<File>( 10 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public JHexOptions( JHexOptions options )
    {
        this();

        if( options.lastAccessedFiles != null )
        {
            lastAccessedFiles.addAll( options.lastAccessedFiles );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public File getLastFile()
    {
        File f = null;

        if( !lastAccessedFiles.isEmpty() )
        {
            f = lastAccessedFiles.first();
        }

        return f;
    }

    public void removeNonExistentRecents()
    {
        List<File> toRemove = new ArrayList<File>();
        for( File f : lastAccessedFiles )
        {
            if( !f.exists() )
            {
                toRemove.add( f );
            }
        }
        for( File f : toRemove )
        {
            lastAccessedFiles.remove( f );
        }
    }
}
