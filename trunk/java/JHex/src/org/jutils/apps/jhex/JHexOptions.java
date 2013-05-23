package org.jutils.apps.jhex;

import java.io.File;

import org.jutils.utils.UniqueMaxStack;

/*******************************************************************************
 * 
 ******************************************************************************/
public class JHexOptions
{
    public final UniqueMaxStack<File> lastAccessedFiles;

    public JHexOptions()
    {
        lastAccessedFiles = new UniqueMaxStack<File>( 10 );
    }

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
