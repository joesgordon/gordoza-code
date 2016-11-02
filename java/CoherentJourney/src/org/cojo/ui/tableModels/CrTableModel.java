package org.cojo.ui.tableModels;

import org.cojo.model.CrState;
import org.cojo.model.IChangeRequest;
import org.jutils.ui.model.ITableItemsConfig;

/*******************************************************************************
 * 
 ******************************************************************************/
public class CrTableModel implements ITableItemsConfig<IChangeRequest>
{
    /**  */
    private static final String [] COLUMN_HEADING = { "#", "Title", "State" };
    /**  */
    private static final Class<?> [] COLUMN_CLASSES = { Integer.class,
        String.class, CrState.class };

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String [] getColumnNames()
    {
        return COLUMN_HEADING;
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
    public Object getItemData( IChangeRequest cr, int col )
    {
        switch( col )
        {
            case 0:
                return cr.getNumber();
            case 1:
                return cr.getTitle();
            case 2:
                return cr.getState();
        }

        throw new IllegalArgumentException( "Unknown column: " + col );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setItemData( IChangeRequest item, int col, Object data )
    {
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean isCellEditable( IChangeRequest item, int col )
    {
        return false;
    }
}
