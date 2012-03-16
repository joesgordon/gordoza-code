package org.jutils.io;

import java.io.File;

/*******************************************************************************
 *
 ******************************************************************************/
public class FileNameAdvancer
{
    private File parentFile = null;

    private String fileName = null;

    private String fileExt = null;

    private int nextIndex = 0;

    /***************************************************************************
     * @param file
     **************************************************************************/
    public FileNameAdvancer( File file )
    {
        String name = file.getName();
        int index = name.lastIndexOf( "." );

        parentFile = file.getParentFile();
        if( index > -1 )
        {
            fileName = name.substring( 0, index );
            fileExt = name.substring( index );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public File getOriginalFile()
    {
        return new File( parentFile, fileName + fileExt );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public File getNext()
    {
        File file = getNext( nextIndex );
        nextIndex++;
        return file;
    }

    /***************************************************************************
     * @param index
     * @return
     **************************************************************************/
    public File getNext( int index )
    {
        File file = null;
        if( index < 1 )
        {
            file = getOriginalFile();
        }
        else
        {
            file = new File( parentFile, fileName + index + fileExt );
        }

        return file;
    }
}
