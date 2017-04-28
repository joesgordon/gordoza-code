package org.eglsht;

import java.awt.Image;
import java.util.List;

import javax.swing.Icon;

import org.jutils.io.IconLoader;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class EagleSheetIcons
{
    public static final IconLoader loader = new IconLoader(
        EagleSheetIcons.class, "icons" );

    /***************************************************************************
     * 
     **************************************************************************/
    private EagleSheetIcons()
    {
    }

    public static Icon getInsertColumnBeforeIcon()
    {
        return loader.getIcon( "table_row_insert.png" );
    }

    public static Icon getInsertColumnAfterIcon()
    {
        return loader.getIcon( "table_row_insert.png" );
    }

    public static Icon getDeleteColumnIcon()
    {
        return loader.getIcon( "table_row_delete.png" );
    }

    public static Icon getInsertRowBeforeIcon()
    {
        return loader.getIcon( "table_row_insert.png" );
    }

    public static Icon getInsertRowAfterIcon()
    {
        return loader.getIcon( "table_row_insert.png" );
    }

    public static Icon getDeleteRowIcon()
    {
        return loader.getIcon( "table_row_delete.png" );
    }

    public static Icon getEditTableIcon()
    {
        return loader.getIcon( "table_edit.png" );
    }

    public static List<Image> getApplicationImages()
    {
        return loader.getImages( "spreadsheet128.png", "spreadsheet64.png",
            "spreadsheet48.png", "spreadsheet32.png", "spreadsheet24.png",
            "spreadsheet16.png" );
    }

    public static Icon getApp32()
    {
        return loader.getIcon( "spreadsheet32.png" );
    }
}
