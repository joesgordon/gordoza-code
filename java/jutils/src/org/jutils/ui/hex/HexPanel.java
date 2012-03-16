package org.jutils.ui.hex;

import java.awt.*;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import org.jutils.PropConstants;

/*******************************************************************************
 * 
 ******************************************************************************/
public class HexPanel
{
    /**  */
    private final HexTable table;
    /**  */
    private final HexRowListModel rowListModel;
    /**  */
    private final JPanel panel;
    /**  */
    private final JList rowHeader;
    /**  */
    private final int rowHeaderFontWidth;

    /***************************************************************************
     * 
     **************************************************************************/
    public HexPanel()
    {
        this.panel = new JPanel( new GridBagLayout() );
        this.rowListModel = new HexRowListModel();
        this.table = new HexTable();
        this.rowHeader = new JList( rowListModel );

        JScrollPane scrollPane = new JScrollPane( table );

        rowHeader.setBackground( ( Color )UIManager.get( PropConstants.UI_PANEL_COLOR ) );
        rowHeader.setFixedCellWidth( 50 );

        // TODO select row when row header is selected

        rowHeader.setFocusable( true );
        rowHeader.setSelectionMode( ListSelectionModel.SINGLE_INTERVAL_SELECTION );
        rowHeader.setFixedCellHeight( table.getRowHeight() );

        // ---------------------------------------------------------------------
        // The calculation for the row header's fixed cell height was the row
        // height + the row margin. This made the rows not line up though. I
        // think it would have worked if the cells in the row header did not
        // have a border.
        // ---------------------------------------------------------------------

        rowHeader.setCellRenderer( new RowHeaderRenderer( table ) );
        rowHeader.setMinimumSize( new Dimension( 50, 5 ) );

        scrollPane.setRowHeaderView( rowHeader );

        panel.add( scrollPane, new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 5,
                5, 5, 5 ), 0, 0 ) );

        FontMetrics fm = rowHeader.getFontMetrics( rowHeader.getFont() );
        rowHeaderFontWidth = fm.charWidth( '0' );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public JComponent getView()
    {
        return panel;
    }

    /***************************************************************************
     * @param address
     **************************************************************************/
    public void setStartingAddress( long address )
    {
        rowListModel.setStartingAddress( address );

        refreshRowHeader();
    }

    /***************************************************************************
     * @param bytes
     **************************************************************************/
    public void setBuffer( IByteBuffer bytes )
    {
        table.setBuffer( bytes );
        rowListModel.setSize( table.getRowCount() );

        refreshRowHeader();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void refreshRowHeader()
    {
        int len = rowListModel.getStringSize();

        len = rowHeaderFontWidth * ( len + 2 );

        rowHeader.setFixedCellWidth( len );
        rowHeader.repaint();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public IByteBuffer getBuffer()
    {
        return table.getBuffer();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class HexRowListModel extends AbstractListModel
    {
        private long startingAddress;
        /**  */
        private int rowCount;
        /**  */
        private String formatString;
        /**  */
        private int stringSize;

        public HexRowListModel()
        {
            startingAddress = 0;
            rowCount = 1;
            stringSize = 1;
            formatString = "%X0";
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
            int w = Long.toHexString( startingAddress + count * 16 - 1 ).length();

            if( w > 0 )
            {
                formatString = "%0" + w + "X";
            }
            else
            {
                formatString = "%X";
            }

            stringSize = w + 1;
        }

        public void setStartingAddress( long address )
        {
            startingAddress = address;
        }

        public Object getElementAt( int index )
        {
            return String.format( formatString, startingAddress + index * 16 );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class RowHeaderRenderer implements ListCellRenderer
    {
        private final JTable table;
        private final TableCellRenderer tcr;
        private final Font f;

        public RowHeaderRenderer( JTable table )
        {
            this.table = table;
            JTableHeader header = table.getTableHeader();
            this.tcr = header.getDefaultRenderer();
            this.f = new Font( "Monospaced", Font.PLAIN, 12 );
        }

        public Component getListCellRendererComponent( JList list,
            Object value, int index, boolean isSelected, boolean cellHasFocus )
        {
            Component c = tcr.getTableCellRendererComponent( table, value,
                isSelected, cellHasFocus, 0, 0 );

            c.setFont( f );

            return c;
        }
    }
}
