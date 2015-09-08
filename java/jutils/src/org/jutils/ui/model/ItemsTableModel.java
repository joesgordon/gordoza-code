package org.jutils.ui.model;

import java.util.*;

import javax.swing.table.AbstractTableModel;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public class ItemsTableModel<T> extends AbstractTableModel
{
    /**  */
    private final List<String> columnNames;
    /**  */
    private final List<Class<?>> columnClasses;
    /**  */
    private final ITableConfig<T> tableConfig;

    /**  */
    private List<T> items;

    /***************************************************************************
     * 
     **************************************************************************/
    public ItemsTableModel( ITableConfig<T> tableConfig )
    {
        super();

        this.tableConfig = tableConfig;

        String [] colName = tableConfig.getColumnNames();
        Class<?> [] colClasses = tableConfig.getColumnClasses();

        this.columnNames = new ArrayList<>();
        this.columnClasses = new ArrayList<>();

        if( columnNames != null )
        {
            columnNames.addAll( Arrays.asList( colName ) );
            columnClasses.addAll( Arrays.asList( colClasses ) );
        }

        this.items = new ArrayList<T>();

        fireTableStructureChanged();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int getColumnCount()
    {
        return columnNames.size();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int getRowCount()
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
    public void setItems( List<? extends T> data )
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
    public void addItem( T item )
    {
        items.add( item );

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
    public T getItem( int row )
    {
        return items.get( row );
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
    public void setColumnNames( List<String> names )
    {
        columnNames.clear();
        columnNames.addAll( names );

        fireTableStructureChanged();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean isCellEditable( int row, int col )
    {
        return tableConfig.isCellEditable( col );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public final Object getValueAt( int row, int col )
    {
        T item = getItem( row );

        if( col < 0 || col > columnNames.size() )
        {
            throw new IllegalArgumentException(
                "Unsupported column index: " + col );
        }

        return tableConfig.getItemData( item, col );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public final void setValueAt( Object data, int row, int col )
    {
        T item = getItem( row );

        if( col < 0 || col > columnNames.size() )
        {
            throw new IllegalArgumentException(
                "Unsupported column index: " + col );
        }

        tableConfig.setItemData( item, col, data );
    }
}
