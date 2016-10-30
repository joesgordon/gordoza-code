package org.cojo.ui.tableModels;

import org.cojo.model.ISoftwareTask;
import org.jutils.ui.model.ITableConfig;

/*******************************************************************************
 * 
 ******************************************************************************/
public class StfTableModel implements ITableConfig<ISoftwareTask>
{
    /**  */
    private static final String [] COLUMN_HEADING = { "#", "Title", "Lead",
        "Est Hours", "Act Hours" };
    /**  */
    private static final Class<?> [] COLUMN_CLASSES = { Integer.class,
        String.class, String.class, Integer.class, Integer.class };

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
    public Object getItemData( ISoftwareTask item, int col )
    {
        switch( col )
        {
            case 0:
                return item.getNumber();
            case 1:
                return item.getTitle();
            case 2:
                return item.getLead().getName();
            case 3:
                return item.getEstimatedHours();
            case 4:
                return item.getActualHours();
        }

        throw new IllegalArgumentException( "Unknown column: " + col );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setItemData( ISoftwareTask item, int col, Object data )
    {
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean isCellEditable( ISoftwareTask item, int col )
    {
        return false;
    }
}
