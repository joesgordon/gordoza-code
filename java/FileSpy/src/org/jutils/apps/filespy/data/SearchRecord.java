package org.jutils.apps.filespy.data;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.filechooser.FileSystemView;

import org.jutils.ui.explorer.ExplorerItem;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SearchRecord implements ExplorerItem
{
    /**  */
    private static final FileSystemView FILE_SYSTEM_VIEW = FileSystemView.getFileSystemView();

    /**  */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
        "MMMM/dd/yyyy kk:mm" );

    /**  */
    private File file = null;

    /**  */
    private Date lastModifiedDate = null;

    /**  */
    private ArrayList<LineMatch> linesFound = new ArrayList<LineMatch>();

    // -------------------------------------------------------------------------
    private String filename = null;

    private String systemName = null;

    private String parentPath = null;

    private long size = -1;

    private String type = null;

    private String lastModified = null;

    /***************************************************************************
     * @param file
     **************************************************************************/
    public SearchRecord( File file )
    {
        super();
        File parent = file.getParentFile();

        this.file = file;
        try
        {
            ;
            type = FILE_SYSTEM_VIEW.getSystemTypeDescription( file );
            systemName = FILE_SYSTEM_VIEW.getSystemDisplayName( file );
        }
        catch( Exception ex )
        {
            type = "?";
            systemName = "?";
        }
        lastModifiedDate = new Date( file.lastModified() );
        filename = file.getName();
        lastModified = DATE_FORMAT.format( lastModifiedDate );
        parentPath = parent == null ? "" : parent.getAbsolutePath();
        size = file.length() / 1024;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public File getFile()
    {
        return file.getAbsoluteFile();
    }

    /***************************************************************************
     * @param line
     **************************************************************************/
    public void addLine( LineMatch line )
    {
        linesFound.add( line );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public List<LineMatch> getLinesFound()
    {
        return new ArrayList<LineMatch>( linesFound );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public String getFilename()
    {
        return filename;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public String getLastModified()
    {
        return lastModified;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public String getParentPath()
    {
        return parentPath;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public long getSizeInKb()
    {
        return size;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public String getType()
    {
        return type != null ? type : "?";
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public String getSystemName()
    {
        return systemName;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public String toString()
    {
        return getSystemName();
    }
}
