package org.cojo.ui.tableModels;

import java.util.Date;

import org.cojo.model.IFinding;
import org.jutils.ui.model.ITableItemsConfig;

/*******************************************************************************
 * 
 ******************************************************************************/
public class FindingTableModel implements ITableItemsConfig<IFinding>
{
    /**  */
    private static final String [] COLUMN_HEADING = { "#", "User", "Date",
        "Accepted" };
    /**  */
    private static final Class<?> [] COLUMN_CLASSES = { Integer.class,
        String.class, Date.class, Boolean.class };

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
    public Object getItemData( IFinding finding, int col )
    {
        switch( col )
        {
            case 0:
                return finding.getNumber();
            case 1:
                return finding.getUser().getName();
            case 2:
                return new Date( finding.getDate() );
            case 3:
                return finding.isAccepted();
        }

        throw new IllegalArgumentException( "Unknown column: " + col );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setItemData( IFinding item, int col, Object data )
    {
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean isCellEditable( IFinding item, int col )
    {
        return false;
    }
}
