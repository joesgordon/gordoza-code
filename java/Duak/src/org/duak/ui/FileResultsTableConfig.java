package org.duak.ui;

import org.duak.data.FileInfo;
import org.duak.utils.FileSize;
import org.jutils.ui.model.ITableConfig;

/*******************************************************************************
 * 
 ******************************************************************************/
public class FileResultsTableConfig implements ITableConfig<FileInfo>
{
    /**  */
    private static Class<?> [] COLUMN_CLASSES = new Class<?>[] { FileInfo.class,
        FileSize.class };
    /**  */
    private static String [] COLUMN_NAMES = new String[] { "Filename", "Size" };

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String [] getColumnNames()
    {
        return COLUMN_NAMES;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Class<?> [] getColumnClasses()
    {
        return COLUMN_CLASSES;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Object getItemData( FileInfo item, int col )
    {
        switch( col )
        {
            case 0:
                return item;
            case 1:
                return new FileSize( item.getSize() );
        }

        throw new IllegalArgumentException(
            "Column " + col + " does not exist in this model." );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setItemData( FileInfo item, int col, Object data )
    {
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean isCellEditable( FileInfo item, int col )
    {
        return false;
    }
}
