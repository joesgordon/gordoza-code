package org.jutils.ui;

import java.awt.Component;
import java.awt.Graphics;
import java.io.File;

import javax.swing.Icon;
import javax.swing.filechooser.FileSystemView;

import org.jutils.IconConstants;

/*******************************************************************************
 * 
 ******************************************************************************/
public class FileIcon implements Icon
{
    /**  */
    private final FileSystemView fileSys;
    /**  */
    private final Icon defaultIcon;
    /**  */
    private final Icon fileIcon;
    /**  */
    private final Icon dirIcon;

    /**  */
    private Icon icon;

    /***************************************************************************
     * 
     **************************************************************************/
    public FileIcon()
    {
        this.fileSys = FileSystemView.getFileSystemView();
        this.defaultIcon = IconConstants.getIcon( IconConstants.INVALID_16 );
        this.fileIcon = IconConstants.getIcon( IconConstants.OPEN_FILE_16 );
        this.dirIcon = IconConstants.getIcon( IconConstants.OPEN_FOLDER_16 );

        this.icon = defaultIcon;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void paintIcon( Component c, Graphics g, int x, int y )
    {
        icon.paintIcon( c, g, x, y );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int getIconWidth()
    {
        return icon.getIconWidth();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int getIconHeight()
    {
        return icon.getIconHeight();
    }

    /***************************************************************************
     * @param file
     **************************************************************************/
    public void setFile( File file )
    {
        if( file == null )
        {
            icon = defaultIcon;
        }
        else if( file.isDirectory() )
        {
            icon = dirIcon;
        }
        else if( file.isFile() )
        {
            icon = fileSys.getSystemIcon( file );

            if( icon == null )
            {
                icon = fileIcon;
            }
        }
    }

    public void setDefaultIcon()
    {
        this.icon = defaultIcon;
    }
}
