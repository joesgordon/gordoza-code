package org.jutils.apps.filespy.data;

import java.io.File;

import org.jutils.io.XObject;
import org.jutils.utils.UniqueMaxQueue;

/*******************************************************************************
 *
 ******************************************************************************/
public class FileSpyData extends XObject
{
    /**  */
    private String lastSavedLocation = null;

    /**  */
    public UniqueMaxQueue<String> filenames = null;

    /**  */
    public UniqueMaxQueue<String> contents = null;

    /**  */
    public UniqueMaxQueue<String> folders = null;

    /***************************************************************************
     * 
     **************************************************************************/
    public FileSpyData()
    {
        super();
        init();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public File getLastSavedLocation()
    {
        if( lastSavedLocation == null )
        {
            return null;
        }
        return new File( lastSavedLocation );
    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    public void setLastSavedLocation( File file )
    {
        lastSavedLocation = file.getAbsolutePath();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void init()
    {
        if( filenames == null )
        {
            filenames = new UniqueMaxQueue<String>();
        }

        if( contents == null )
        {
            contents = new UniqueMaxQueue<String>();
        }

        if( folders == null )
        {
            folders = new UniqueMaxQueue<String>();
        }
    }
}
