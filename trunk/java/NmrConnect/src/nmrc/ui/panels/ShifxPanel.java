package nmrc.ui.panels;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.*;

import nmrc.model.IShiftxRecord;
import nmrc.ui.tables.ItemTable;
import nmrc.ui.tables.models.ShiftxTableModel;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ShifxPanel extends JPanel
{
    /**  */
    private ItemTable<IShiftxRecord> table;

    /***************************************************************************
     * 
     **************************************************************************/
    public ShifxPanel()
    {
        super();

        // ---------------------------------------------------------------------
        // Setup records scroll pane.
        // ---------------------------------------------------------------------
        table = new ItemTable<IShiftxRecord>( new ShiftxTableModel() );
        JScrollPane recordsScrollPane = new JScrollPane( table );

        table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );

        // ---------------------------------------------------------------------
        // Setup this panel.
        // ---------------------------------------------------------------------
        setLayout( new BorderLayout() );

        add( recordsScrollPane, BorderLayout.CENTER );
    }

    /***************************************************************************
     * @param records
     **************************************************************************/
    public void setData( List<IShiftxRecord> records )
    {
        table.getTableModel().setItems( records );
    }
}
