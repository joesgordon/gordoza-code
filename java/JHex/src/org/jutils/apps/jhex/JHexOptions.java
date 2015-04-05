package org.jutils.apps.jhex;

import java.io.File;

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
        lastAccessedFiles = new MaxQueue<File>( 10 );
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
}
