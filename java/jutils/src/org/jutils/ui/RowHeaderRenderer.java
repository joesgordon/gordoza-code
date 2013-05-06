package org.jutils.ui;

import java.awt.Component;
import java.awt.Font;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RowHeaderRenderer implements ListCellRenderer
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

    public Component getListCellRendererComponent( JList list, Object value,
        int index, boolean isSelected, boolean cellHasFocus )
    {
        Component c = tcr.getTableCellRendererComponent( table, value,
            isSelected, cellHasFocus, 0, 0 );

        c.setFont( f );

        return c;
    }
}
