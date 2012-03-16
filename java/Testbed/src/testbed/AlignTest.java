package testbed;

import javax.swing.*;
import javax.swing.table.*;

public class AlignTest
{
    private JScrollPane getContent()
    {
        JTable table = new JTable( getModel() );
        java.awt.Dimension d = table.getPreferredSize();
        table.setPreferredScrollableViewportSize( d );
        // You can set renderers for any TableColumn.
        // Set a renderer with center horizontal alignment in
        // TableColumn zero.
        TableColumn col = table.getColumnModel().getColumn( 0 );
        DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
        dtcr.setHorizontalAlignment( SwingConstants.CENTER );
        col.setCellRenderer( dtcr );
        // Default cell renderers are assigned/set by column class.
        // You can fetch default renderers and reconfigure them or
        // set new default renderers.
        // Query and alter the Integer class default cell renderer.
        TableCellRenderer tcr = table.getDefaultRenderer( Integer.class );
        System.out.println( "tcr = " + tcr.getClass().getName() );
        System.out.printf( "tcr extends DefaultTableCellRenderer = %b%n",
            DefaultTableCellRenderer.class.isAssignableFrom( tcr.getClass() ) );
        DefaultTableCellRenderer renderer = ( DefaultTableCellRenderer )tcr;
        System.out.println( "renderer horizontalAlignment = " +
            renderer.getHorizontalAlignment() ); // 4:right
        renderer.setHorizontalAlignment( SwingConstants.CENTER );
        return new JScrollPane( table );
    }

    private AbstractTableModel getModel()
    {
        return new AbstractTableModel()
        {
            public int getRowCount()
            {
                return 6;
            }

            public int getColumnCount()
            {
                return 4;
            }

            public Class<?> getColumnClass( int col )
            {
                return getValueAt( 0, col ).getClass();
            }

            public Object getValueAt( int row, int col )
            {
                int r = row + 1;
                int c = col + 1;
                if( col < 2 )
                {
                    return String.valueOf( r ) + c;
                }
                else
                {
                    return Integer.valueOf( 10 * r + c );
                }
            }
        };
    }

    public static void main( String[] args )
    {
        AlignTest test = new AlignTest();
        JOptionPane.showMessageDialog( null, test.getContent(), "",
            JOptionPane.PLAIN_MESSAGE );
    }
}
