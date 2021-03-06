package testbed;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.jutils.core.data.UIProperty;
import org.jutils.core.io.LogUtils;
import org.jutils.core.ui.StandardFrameView;
import org.jutils.core.ui.app.FrameRunner;
import org.jutils.core.ui.app.IFrameApp;
import org.jutils.core.ui.model.IView;

public class TableButtonDemo implements IView<JComponent>
{
    private final JComponent view;

    public TableButtonDemo()
    {
        String[] columnNames = { "Date", "String", "Integer", "Decimal", "" };
        Object[][] data = { { new Date(), "A", 1, 5.1, "Delete0" },
            { new Date(), "B", 2, 6.2, "Delete1" },
            { new Date(), "C", 3, 7.3, "Delete2" },
            { new Date(), "D", 4, 8.4, "Delete3" } };

        DefaultTableModel model = new DefaultTableModel( data, columnNames );
        JTable table = new JTable( model )
        {
            private static final long serialVersionUID = 1L;

            // Returning the Class of each column will allow different
            // renderers to be used based on Class
            @Override
            public Class<?> getColumnClass( int column )
            {
                return getValueAt( 0, column ).getClass();
            }
        };
        table.setRowHeight( 30 );

        JScrollPane scrollPane = new JScrollPane( table );

        // Create button column
        new ButtonColumn( table, 4 );

        this.view = scrollPane;
    }

    @Override
    public JComponent getView()
    {
        return view;
    }

    public static void main( String[] args )
    {
        FrameRunner.invokeLater( new IFrameApp()
        {
            @Override
            public void finalizeGui()
            {
            }

            @Override
            public JFrame createFrame()
            {
                StandardFrameView frame = new StandardFrameView();
                TableButtonDemo demo = new TableButtonDemo();

                frame.setContent( demo.getView() );
                frame.setSize( 800, 400 );
                frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

                return frame.getView();
            }
        } );
    }

    private static class ButtonColumn extends AbstractCellEditor
        implements TableCellRenderer, TableCellEditor, ActionListener
    {
        private static final long serialVersionUID = -1002665205717378521L;

        private final JTable table;

        private final JButton renderButton;

        private final JButton editButton;

        private String text;

        public ButtonColumn( JTable table, int column )
        {
            super();
            this.table = table;
            renderButton = new JButton();

            editButton = new JButton();
            editButton.setFocusPainted( false );
            editButton.addActionListener( this );

            TableColumnModel columnModel = table.getColumnModel();
            columnModel.getColumn( column ).setCellRenderer( this );
            columnModel.getColumn( column ).setCellEditor( this );
        }

        @Override
        public Component getTableCellRendererComponent( JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row,
            int column )
        {
            if( hasFocus )
            {
                renderButton.setForeground( table.getForeground() );
                renderButton.setBackground(
                    UIProperty.BUTTON_BACKGROUND.getColor() );
            }
            else if( isSelected )
            {
                renderButton.setForeground( table.getSelectionForeground() );
                renderButton.setBackground( table.getSelectionBackground() );
            }
            else
            {
                renderButton.setForeground( table.getForeground() );
                renderButton.setBackground(
                    UIProperty.BUTTON_BACKGROUND.getColor() );
            }

            renderButton.setText( ( value == null ) ? "" : value.toString() );
            return renderButton;
        }

        @Override
        public Component getTableCellEditorComponent( JTable table,
            Object value, boolean isSelected, int row, int column )
        {
            text = ( value == null ) ? "" : value.toString();
            editButton.setText( text );
            return editButton;
        }

        @Override
        public Object getCellEditorValue()
        {
            return text;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            fireEditingStopped();
            LogUtils.printDebug(
                e.getActionCommand() + " : " + table.getSelectedRow() );
        }
    }
}
