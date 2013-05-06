package org.jutils.ui.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

// TODO this is really broken. fix it.

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public abstract class ItemTableModel<T> extends AbstractTableModel
{
    /**  */
    private List<Object> columnNames;
    /**  */
    private List<Class<?>> columnClasses;
    /**  */
    private List<T> items;

    /***************************************************************************
     * 
     **************************************************************************/
    public ItemTableModel()
    {
        super();

        columnNames = new ArrayList<Object>();
        columnClasses = new ArrayList<Class<?>>();
        items = new ArrayList<T>();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public final int getColumnCount()
    {
        return columnNames.size();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public final int getRowCount()
    {
        return items.size();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public List<T> getItems()
    {
        return new ArrayList<T>( items );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Class<?> getColumnClass( int col )
    {
        Class<?> cls;
        if( columnClasses.isEmpty() && col < getColumnCount() )
        {
            cls = String.class;
        }
        else
        {
            cls = columnClasses.get( col );
        }
        return cls;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getColumnName( int col )
    {
        return columnNames.get( col ).toString();
    }

    /***************************************************************************
     * @param names
     **************************************************************************/
    public void setColumnClasses( List<Class<?>> classes )
    {
        columnClasses.clear();
        columnClasses.addAll( classes );
        fireTableStructureChanged();
    }

    /***************************************************************************
     * @param names
     **************************************************************************/
    public void setColumnNames( List<? extends Object> names )
    {
        columnNames.clear();
        columnNames.addAll( names );

        fireTableStructureChanged();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public List<? extends Object> getColumnNames()
    {
        return columnNames;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void clearItems()
    {
        int lastRow = items.size() - 1;
        items.clear();
        if( lastRow > -1 )
        {
            super.fireTableRowsDeleted( 0, lastRow );
        }
    }

    /***************************************************************************
     * @param data
     **************************************************************************/
    public void setItems( List<T> data )
    {
        int lastRow;

        clearItems();
        this.items.addAll( data );

        lastRow = this.items.size() - 1;
        if( lastRow > -1 )
        {
            super.fireTableRowsInserted( 0, lastRow );
        }
    }

    /***************************************************************************
     * @param rowData
     **************************************************************************/
    public void addRow( T rowData )
    {
        items.add( rowData );
        super.fireTableRowsInserted( items.size() - 1, items.size() - 1 );
    }

    /***************************************************************************
     * @param row
     **************************************************************************/
    public void removeRow( int row )
    {
        items.remove( row );
        super.fireTableRowsDeleted( row, row );
    }

    /***************************************************************************
     * @param row
     * @return
     **************************************************************************/
    public final T getRow( int row )
    {
        return items.get( row );
    }
}
