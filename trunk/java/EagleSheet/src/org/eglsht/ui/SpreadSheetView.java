package org.eglsht.ui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;

import org.eglsht.model.ISpreadSheet;
import org.jutils.PropConstants;
import org.jutils.ui.RowHeaderRenderer;
import org.jutils.ui.model.IDataView;

public class SpreadSheetView implements IDataView<ISpreadSheet>
{
    /**  */
    private final JPanel view;
    /**  */
    private final JScrollPane scrollpane;
    /**  */
    private final JTable table;
    /**  */
    private final SheetModel model;
    /**  */
    private final RowListModel rowHeaderModel;
    /**  */
    private final JList rowHeader;
    /**  */
    private final int rowHeaderFontWidth;
    /**  */
    private final JLabel cornerLabel;

    /**
     * 
     */
    public SpreadSheetView()
    {
        this.rowHeaderModel = new RowListModel();
        this.rowHeader = new JList( rowHeaderModel );
        this.model = new SheetModel();
        this.table = new JTable( model );
        this.scrollpane = new JScrollPane( table );
        this.cornerLabel = new JLabel();
        this.view = new JPanel( new BorderLayout() );

        rowHeader.setSelectionMode( ListSelectionModel.SINGLE_INTERVAL_SELECTION );
        rowHeader.setCellRenderer( new RowHeaderRenderer( table ) );
        rowHeader.setFixedCellHeight( table.getRowHeight() );
        rowHeader.setMinimumSize( new Dimension( 50, 5 ) );
        rowHeader.setBackground( ( Color )UIManager.get( PropConstants.UI_PANEL_COLOR ) );
        rowHeader.setFixedCellWidth( 50 );

        table.setSelectionMode( ListSelectionModel.SINGLE_INTERVAL_SELECTION );
        table.setColumnSelectionAllowed( true );
        table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );

        scrollpane.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS );
        scrollpane.setVerticalScrollBarPolicy( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
        scrollpane.setRowHeaderView( rowHeader );
        scrollpane.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );
        scrollpane.setCorner( ScrollPaneConstants.UPPER_LEFT_CORNER,
            cornerLabel );

        view.add( scrollpane, BorderLayout.CENTER );

        FontMetrics fm = rowHeader.getFontMetrics( rowHeader.getFont() );
        this.rowHeaderFontWidth = fm.charWidth( 'W' );
    }

    @Override
    public Component getView()
    {
        return view;
    }

    @Override
    public ISpreadSheet getData()
    {
        return model.getData();
    }

    @Override
    public void setData( ISpreadSheet data )
    {
        model.setData( data );
        rowHeaderModel.setSize( data.getRowCount() );
        refreshRowHeader();
        cornerLabel.setText( data.getCornerName() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void refreshRowHeader()
    {
        int len = rowHeaderModel.getStringSize();

        len = rowHeaderFontWidth * ( len + 2 );

        rowHeader.setFixedCellWidth( len );
        rowHeader.repaint();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class SheetModel extends AbstractTableModel
    {
        private ISpreadSheet sheet;

        public SheetModel()
        {
            sheet = new NullSheet();
        }

        public void setData( ISpreadSheet file )
        {
            this.sheet = file;

            super.fireTableStructureChanged();

            System.out.println( "Size set to " + file.getRowCount() );
        }

        public ISpreadSheet getData()
        {
            return sheet;
        }

        @Override
        public boolean isCellEditable( int row, int col )
        {
            return true;
        }

        @Override
        public String getColumnName( int col )
        {
            String name = sheet.getColumnHeader( col );

            if( name == null )
            {
                name = generateDefautColumnName( col );
            }

            return name;
        }

        @Override
        public Object getValueAt( int row, int col )
        {
            return sheet.getValueAt( row, col );
        }

        @Override
        public void setValueAt( Object value, int row, int col )
        {
            sheet.setValueAt( value.toString(), row, col );
        }

        @Override
        public int getRowCount()
        {
            return sheet.getRowCount();
        }

        @Override
        public int getColumnCount()
        {
            return sheet.getColumnCount();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class RowListModel extends AbstractListModel
    {
        /**  */
        private int rowCount;
        /**  */
        private int stringSize;

        public RowListModel()
        {
            rowCount = 1;
            stringSize = 1;
        }

        @Override
        public int getSize()
        {
            return rowCount;
        }

        public int getStringSize()
        {
            return stringSize;
        }

        public void setSize( int count )
        {
            rowCount = count;
            int w = Integer.toString( count ).length();

            stringSize = w + 1;

            super.fireContentsChanged( this, 0, count );
        }

        public Object getElementAt( int index )
        {
            return Integer.toString( index + 1 );
        }
    }

    private static class NullSheet implements ISpreadSheet
    {
        @Override
        public int getColumnCount()
        {
            return 0;
        }

        @Override
        public int getRowCount()
        {
            return 0;
        }

        @Override
        public String getValueAt( int row, int col )
        {
            return null;
        }

        @Override
        public String getColumnHeader( int col )
        {
            return null;
        }

        @Override
        public String getRowHeader( int row )
        {
            return null;
        }

        @Override
        public void setValueAt( String string, int row, int col )
        {
            ;
        }

        @Override
        public String getCornerName()
        {
            return null;
        }
    }

    private static String generateDefautColumnName( int col )
    {
        String name = "";
        int val;
        char c;

        do
        {
            val = col % 26;
            c = ( char )( ( int )'A' + val );
            name = c + name;

            col /= 26;
            col--;
        } while( col > -1 );

        return name;
    }
}
