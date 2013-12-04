package org.jutils.ui;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ResizingTable<T extends TableModel> extends JTable
{
    /**  */
    private T model;

    /***************************************************************************
     * 
     **************************************************************************/
    public ResizingTable( T model )
    {
        super( model );
        this.model = model;

        model.addTableModelListener( new TableChangedListener<T>( this ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public T getTableModel()
    {
        return model;
    }

    /***************************************************************************
     *
     **************************************************************************/
    private void resizeTable()
    {
        int horzSpace = 6;
        String colName;
        int colCount = model.getColumnCount();
        int rowCount = model.getRowCount();
        int widths[] = new int[model.getColumnCount()];
        Component cellRenderer;
        TableCellRenderer tableCellRenderer;
        int totalWidth = 0;
        int defaultWidth;

        // ---------------------------------------------------------------------
        // computes headers' widths
        // ---------------------------------------------------------------------
        for( int col = 0; col < colCount; col++ )
        {
            colName = model.getColumnName( col );
            defaultWidth = col < colCount - 1 ? 65 : getWidth() - totalWidth;

            tableCellRenderer = getColumnModel().getColumn( col ).getHeaderRenderer();
            if( tableCellRenderer == null )
            {
                tableCellRenderer = getTableHeader().getDefaultRenderer();
            }
            cellRenderer = tableCellRenderer.getTableCellRendererComponent(
                this, colName, false, false, -1, col );

            widths[col] = ( int )cellRenderer.getPreferredSize().getWidth() +
                horzSpace;
            widths[col] = Math.max( widths[col], defaultWidth );

            tableCellRenderer = getCellRenderer( -1, col );

            // -----------------------------------------------------------------
            // check if cell values fit in their cells
            // -----------------------------------------------------------------
            for( int row = 0; row < rowCount; row++ )
            {
                Object obj = model.getValueAt( row, col );
                int width = 0;
                if( obj != null )
                {
                    tableCellRenderer = getCellRenderer( row, col );
                    cellRenderer = tableCellRenderer.getTableCellRendererComponent(
                        this, obj, false, false, row, col );
                    width = ( int )cellRenderer.getPreferredSize().getWidth() +
                        horzSpace;
                }
                widths[col] = Math.max( widths[col], width );
            }

            totalWidth += widths[col];
        }

        TableColumnModel colModel = getColumnModel();

        // ---------------------------------------------------------------------
        // set the column widths
        // ---------------------------------------------------------------------
        for( int i = 0; i < colCount; i++ )
        {
            colModel.getColumn( i ).setPreferredWidth( widths[i] );
        }
    }

    private static class TableChangedListener<T extends TableModel> implements
        TableModelListener
    {
        private ResizingTable<T> resizingTable;

        public TableChangedListener( ResizingTable<T> resizingTable )
        {
            this.resizingTable = resizingTable;
        }

        @Override
        public void tableChanged( TableModelEvent e )
        {
            SwingUtilities.invokeLater( new Runnable()
            {
                @Override
                public void run()
                {
                    resizingTable.resizeTable();
                }
            } );
        }
    }
}
