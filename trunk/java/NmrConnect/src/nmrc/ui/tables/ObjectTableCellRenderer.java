package nmrc.ui.tables;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ObjectTableCellRenderer extends DefaultTableCellRenderer
{
    /**  */
    private DefaultTableCellRenderer origRenderer;
    /**  */
    private Color origBackground;

    /***************************************************************************
     * 
     **************************************************************************/
    public ObjectTableCellRenderer( DefaultTableCellRenderer renderer )
    {
        origRenderer = renderer;

        if( origRenderer != null )
        {
            origRenderer.setOpaque( true );
            origBackground = origRenderer.getBackground();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Component getTableCellRendererComponent( JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int column )
    {
        Component comp;

        if( origRenderer != null && row > -1 )
        {
            comp = origRenderer.getTableCellRendererComponent( table, value,
                isSelected, hasFocus, row, column );
        }
        else
        {
            comp = super.getTableCellRendererComponent( table, value,
                isSelected, hasFocus, row, column );
        }

        if( row > -1 )
        {
            Object val = table.getModel().getValueAt( row, column );
            if( !isSelected )
            {
                if( val == null )
                {
                    comp.setBackground( Color.lightGray );
                }
                else
                {
                    comp.setBackground( origBackground );
                }
            }
        }

        return comp;
    }
}
