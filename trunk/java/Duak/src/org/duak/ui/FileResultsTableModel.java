package org.duak.ui;

import java.util.Arrays;

import org.duak.data.FileInfo;
import org.duak.utils.FileSize;
import org.jutils.ui.model.ItemTableModel;

/*******************************************************************************
 * 
 ******************************************************************************/
public class FileResultsTableModel extends ItemTableModel<FileInfo>
{
    /**  */
    private static Class<?> [] classes = new Class<?>[] { FileInfo.class,
        FileSize.class };
    /**  */
    private static String [] column_names = new String[] { "Filename", "Size" };

    /***************************************************************************
     * 
     **************************************************************************/
    public FileResultsTableModel()
    {
        setColumnClasses( Arrays.asList( classes ) );
        setColumnNames( Arrays.asList( column_names ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Object getValueAt( int row, int col )
    {
        FileInfo fr = getRow( row );

        switch( col )
        {
            case 0:
                return fr;
            case 1:
                return new FileSize( fr.getSize() );
        }

        throw new IllegalArgumentException( "Column " + col +
            " does not exist in this model." );
    }
}
