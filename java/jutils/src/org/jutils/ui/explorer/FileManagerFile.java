package org.jutils.ui.explorer;

import java.io.File;
import java.util.Hashtable;

import org.jutils.io.XObject;

/*******************************************************************************
 * TODO Finish this class.
 ******************************************************************************/
public class FileManagerFile extends XObject
{
    private Hashtable<String, File> fileTable = null;

    /***************************************************************************
     * 
     **************************************************************************/
    public FileManagerFile()
    {
        init();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    protected void init()
    {
        if( fileTable == null )
        {
            fileTable = new Hashtable<String, File>();
        }
    }

}
