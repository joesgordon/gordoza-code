package org.cc.view;

import javax.swing.Icon;

import org.jutils.IconConstants;
import org.jutils.IconLoader;

public class CvIconLoader
{
    private static final IconLoader loader = new IconLoader(
        CvIconLoader.class, "icons" );

    public static Icon getCheckoutIcon()
    {
        return loader.getIcon( "Check24.png" );
    }

    public static Icon getAssignIcon()
    {
        return loader.getIcon( "database_add.png" );
    }

    public static Icon getUnassignIcon()
    {
        return loader.getIcon( "db_remove.png" );
    }

    public static Icon getRevertIcon()
    {
        return IconConstants.loader.getIcon( IconConstants.UNDO_16 );
    }

    public static Icon getRemoveIcon()
    {
        return IconConstants.loader.getIcon( IconConstants.EDIT_DELETE_16 );
    }

    public static Icon getFileIcon()
    {
        return loader.getIcon( "folder24.png" );
    }

    public static Icon getRefreshIcon()
    {
        return IconConstants.loader.getIcon( IconConstants.REFRESH_16 );
    }

    public static Icon getUpdateIcon()
    {
        return loader.getIcon( "svn-update.png" );
    }

    public static Icon getCommitIcon()
    {
        return loader.getIcon( "svn-commit.png" );
    }

    public static Icon getAddIcon()
    {
        return IconConstants.loader.getIcon( IconConstants.EDIT_ADD_16 );
    }
}
