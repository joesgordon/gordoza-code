package org.jutils.apps.filespy.data;

import java.io.File;

import org.jutils.utils.UniqueMaxStack;

/*******************************************************************************
 *
 ******************************************************************************/
public class FileSpyData
{
    /**  */
    public File lastSavedLocation = null;

    /**  */
    public final UniqueMaxStack<String> filenames;

    /**  */
    public final UniqueMaxStack<String> contents;

    /**  */
    public final UniqueMaxStack<String> folders;

    /***************************************************************************
     * 
     **************************************************************************/
    public FileSpyData()
    {
        this.filenames = new UniqueMaxStack<String>();
        this.contents = new UniqueMaxStack<String>();
        this.folders = new UniqueMaxStack<String>();
    }

    /***************************************************************************
     * @param data
     **************************************************************************/
    public FileSpyData( FileSpyData data )
    {
        this();

        this.lastSavedLocation = data.lastSavedLocation;

        if( data.filenames != null )
        {
            this.filenames.pushAll( data.filenames );
        }

        if( data.contents != null )
        {
            this.contents.pushAll( data.contents );
        }

        if( data.folders != null )
        {
            this.folders.pushAll( data.folders );
        }
    }
}
