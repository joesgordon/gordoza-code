package org.jutils.ui.explorer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.filechooser.FileSystemView;

public class DefaultExplorerItem implements ExplorerItem
{
    /**  */
    public static final FileSystemView FILE_SYSTEM_VIEW = FileSystemView.getFileSystemView();

    /**  */
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
        "MMMM/dd/yyyy kk:mm" );

    /**  */
    private File file = null;

    /**  */
    private String systemName = null;

    /***************************************************************************
     * @param file
     **************************************************************************/
    public DefaultExplorerItem( File file )
    {
        this.file = file;
    }

    /***************************************************************************
	 * 
	 **************************************************************************/
    public String getFilename()
    {
        return file.getName();
    }

    /***************************************************************************
	 * 
	 **************************************************************************/
    public String getLastModified()
    {
        Date d = new Date( file.lastModified() );
        return DATE_FORMAT.format( d );
    }

    /***************************************************************************
	 * 
	 **************************************************************************/
    public String getParentPath()
    {
        File parent = file.getParentFile();
        return parent == null ? "" : parent.getAbsolutePath();
    }

    /***************************************************************************
	 * 
	 **************************************************************************/
    public long getSizeInKb()
    {
        long len = file.length() / 1024;
        return file.isDirectory() ? -1 : len;
    }

    /***************************************************************************
	 * 
	 **************************************************************************/
    public String getType()
    {
        String desc = FILE_SYSTEM_VIEW.getSystemTypeDescription( file );

        if( desc == null )
        {
            if( file.isFile() )
            {
                desc = getExtension();
                desc = desc.toUpperCase();
                if( desc.length() > 0 )
                {
                    desc += " ";
                }
                desc += "File";
            }
            else
            {
                desc = "Folder";
            }
        }

        return desc;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String getExtension()
    {
        String name = file.getName();
        int idx = name.lastIndexOf( '.' );

        if( idx > -1 && idx + 1 < name.length() )
        {
            name = name.substring( idx + 1 );
        }
        else
        {
            name = "";
        }

        return name;
    }

    /***************************************************************************
	 * 
	 **************************************************************************/
    public String getSystemName()
    {
        if( systemName == null )
        {
            systemName = FILE_SYSTEM_VIEW.getSystemDisplayName( file );
        }
        return systemName;
    }

    /***************************************************************************
	 * 
	 **************************************************************************/
    public File getFile()
    {
        return file;
    }

    /***************************************************************************
     * @return String
     **************************************************************************/
    public String toString()
    {
        return getSystemName();
    }
}
