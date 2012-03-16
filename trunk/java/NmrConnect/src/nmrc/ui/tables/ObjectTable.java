package nmrc.ui.tables;

import java.awt.Color;
import java.util.*;

import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;

import org.jutils.ui.ResizingTable;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public class ObjectTable<T extends Object, M extends TableModel> extends
    ResizingTable<M>
{
    /**  */
    private List<ObjectTableCellRenderer> renderers;

    /***************************************************************************
     * 
     **************************************************************************/
    public ObjectTable( M model )
    {
        super( model );

        setGridColor( Color.black );

        renderers = new ArrayList<ObjectTableCellRenderer>();

        setModel( model );

        createCellRenderers();

        model.addTableModelListener( new TableModelListener()
        {
            @Override
            public void tableChanged( TableModelEvent e )
            {
                SwingUtilities.invokeLater( new Runnable()
                {
                    @Override
                    public void run()
                    {
                        updateTable();
                    }
                } );
            }
        } );

        updateTable();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void createCellRenderers()
    {
        TableCellRenderer tcr;
        ObjectTableCellRenderer cellRenderer;
        Class<?> cls;

        for( int i = 0; i < getColumnCount(); i++ )
        {
            cls = getColumnClass( i );
            tcr = getDefaultRenderer( cls );
            cellRenderer = new ObjectTableCellRenderer(
                ( DefaultTableCellRenderer )tcr );
            renderers.add( cellRenderer );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void updateTable()
    {
        Enumeration<TableColumn> cols = getColumnModel().getColumns();
        Iterator<ObjectTableCellRenderer> tcrs = renderers.iterator();

        while( cols.hasMoreElements() )
        {
            cols.nextElement().setCellRenderer( tcrs.next() );
        }
    }
}
