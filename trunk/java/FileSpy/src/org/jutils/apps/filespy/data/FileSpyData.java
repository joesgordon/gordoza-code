package org.jutils.apps.filespy.data;

import java.io.File;

import org.jutils.io.XObject;
import org.jutils.utils.UniqueMaxStack;

/*******************************************************************************
 *
 ******************************************************************************/
public class FileSpyData extends XObject
{
    /**  */
    private String lastSavedLocation = null;

    /**  */
    public UniqueMaxStack<String> filenames = null;

    /**  */
    public UniqueMaxStack<String> contents = null;

    /**  */
    public UniqueMaxStack<String> folders = null;

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
            filenames = new UniqueMaxStack<String>();
        }

        if( contents == null )
        {
            contents = new UniqueMaxStack<String>();
        }

        if( folders == null )
        {
            folders = new UniqueMaxStack<String>();
        }
    }
}
