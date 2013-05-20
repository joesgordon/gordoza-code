package org.jutils.ui.hex;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.jutils.PropConstants;
import org.jutils.ui.RowHeaderRenderer;
import org.jutils.ui.hex.HexTable.IRangeSelectedListener;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class HexPanel implements IView<JComponent>
{
    /**  */
    private final JPanel panel;
    /**  */
    private final HexTable table;
    /**  */
    private final HexRowListModel rowListModel;
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

        scrollPane.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );
        scrollPane.setRowHeaderView( rowHeader );

        panel.add( scrollPane, new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 0,
                0, 0, 0 ), 0, 0 ) );

        Dimension dim = panel.getMinimumSize();
        dim.width = 600;
        panel.setMinimumSize( dim );

        FontMetrics fm = rowHeader.getFontMetrics( rowHeader.getFont() );
        rowHeaderFontWidth = fm.charWidth( '0' );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addRangeSelectedListener( IRangeSelectedListener l )
    {
        table.addRangeSelectedListener( l );
    }

    public int getSelectedColumn()
    {
        return table.getSelectedColumn();
    }

    public int getSelectedRow()
    {
        return table.getSelectedRow();
    }

    public void setHightlightColor( Color c )
    {
        table.setHightlightColor( c );
    }

    public void setHighlightLength( int length )
    {
        table.setHighlightLength( length );
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
        /**  */
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
}
