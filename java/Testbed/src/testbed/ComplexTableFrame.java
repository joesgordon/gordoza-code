package testbed;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;

import org.jutils.IconConstants;
import org.jutils.Utils;
import org.jutils.io.LogUtils;

import com.jgoodies.looks.Options;

/***************************************************************************
 * 
 **************************************************************************/
public class ComplexTableFrame extends JFrame
{
    private static final String nimbusLaf = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";

    private static final String jgoodiesLaf = Options.PLASTICXP_NAME;

    /***************************************************************************
     * 
     **************************************************************************/
    public ComplexTableFrame()
    {
        final JButton nimbusButton = new JButton( "Nimbus" );
        final JButton jgoodiesButton = new JButton( "JGoodies" );
        Dimension buttonSize = Utils.getMaxComponentSize( new Component[] {
            nimbusButton, jgoodiesButton } );
        CustomTableModel tableModel = new CustomTableModel();
        JTable table = new JTable( tableModel )
        {
            // Returning the Class of each column will allow different
            // renderers to be used based on Class
            public Class<?> getColumnClass( int col )
            {
                return this.getModel().getColumnClass( col );
            }
        };
        TableColumn column = table.getColumnModel().getColumn( 2 );
        final StateColumnCell columnCell = new StateColumnCell( table );
        TableData data;
        JScrollPane tableScrollPane = new JScrollPane( table );

        column.setCellEditor( columnCell );
        column.setCellRenderer( columnCell );

        data = new TableData();
        data.text = "Row # 1";
        data.num = 9;
        data.state = State.Confirmed;
        tableModel.addData( data );

        data = new TableData();
        data.text = "Row # 2";
        data.num = 19;
        data.state = State.Done;
        tableModel.addData( data );

        data = new TableData();
        data.text = "Row # 3";
        data.num = 29;
        data.state = State.NotDone;
        tableModel.addData( data );

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout( new GridBagLayout() );

        nimbusButton.setPreferredSize( buttonSize );
        nimbusButton.setEnabled( false );
        nimbusButton.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                nimbusButton.setEnabled( false );
                jgoodiesButton.setEnabled( true );
                resetLaf( nimbusLaf );
                columnCell.validate();
            }
        } );
        jgoodiesButton.setPreferredSize( buttonSize );
        jgoodiesButton.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                jgoodiesButton.setEnabled( false );
                nimbusButton.setEnabled( true );
                resetLaf( jgoodiesLaf );
                columnCell.validate();
            }
        } );

        table.setShowGrid( false );
        table.setRowHeight( 30 );
        Dimension spacing = table.getIntercellSpacing();
        spacing.width += 3;
        table.setIntercellSpacing( spacing );
        table.setBackground( mainPanel.getBackground() );
        // table.setSelectionMode( ListSelectionModel.SINGLE_INTERVAL_SELECTION
        // );
        // table.setCellSelectionEnabled( false );

        mainPanel.add( nimbusButton, new GridBagConstraints( 0, 0, 1, 1, 1.0,
            0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 2, 2, 2, 2 ), 0, 0 ) );
        mainPanel.add( jgoodiesButton, new GridBagConstraints( 1, 0, 1, 1, 1.0,
            0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 2, 2, 2, 2 ), 0, 0 ) );

        mainPanel.add( tableScrollPane, new GridBagConstraints( 0, 1, 2, 1,
            0.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 2, 2, 2, 2 ), 0, 0 ) );

        setContentPane( mainPanel );
    }

    /***************************************************************************
     * @param laf
     **************************************************************************/
    private void resetLaf( String laf )
    {
        try
        {
            UIManager.setLookAndFeel( laf );
            SwingUtilities.updateComponentTreeUI( this );
            this.validate();
        }
        catch( Exception ex )
        {
            ex.printStackTrace();
        }
    }

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( new Runnable()
        {
            public void run()
            {
                try
                {
                    UIManager.setLookAndFeel( jgoodiesLaf );
                    Options.setSelectOnFocusGainEnabled( true );
                }
                catch( Exception ex )
                {
                    ex.printStackTrace();
                }

                JFrame frame = new ComplexTableFrame();

                frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
                frame.setSize( 500, 500 );
                frame.validate();
                frame.setMinimumSize( frame.getSize() );
                frame.setLocationRelativeTo( null );
                frame.setVisible( true );
            }
        } );
    }
}

/*******************************************************************************
 * 
 ******************************************************************************/
enum State
{
    NotDone, Done, Confirmed
}

/*******************************************************************************
 * 
 ******************************************************************************/
class TableData
{
    /**  */
    public String text;

    /**  */
    public int num;

    /**  */
    public State state;
}

/*******************************************************************************
 * 
 ******************************************************************************/
class StateColumnCell extends AbstractCellEditor implements TableCellRenderer,
    TableCellEditor
{
    /**  */
    private TableDataCellPanel rendererCellPanel;

    /**  */
    private TableDataCellPanel editorCellPanel;

    /**  */
    private JTable table;

    /**  */
    private State state;

    /***************************************************************************
     * @param table
     **************************************************************************/
    public StateColumnCell( JTable table )
    {
        this.table = table;

        rendererCellPanel = new TableDataCellPanel();
        editorCellPanel = new TableDataCellPanel();

        editorCellPanel.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                buttonPressed( e );
            }
        } );
    }

    public void validate()
    {
        SwingUtilities.updateComponentTreeUI( rendererCellPanel );
        SwingUtilities.updateComponentTreeUI( editorCellPanel );
    }

    /***************************************************************************
     * @param e
     **************************************************************************/
    private void buttonPressed( ActionEvent e )
    {
        fireEditingStopped();
        LogUtils.printDebug( state + " : " + table.getSelectedRow() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public Component getTableCellRendererComponent( JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int column )
    {
        State state = ( State )value;
        Color bg = null;
        Color fg;
        rendererCellPanel.setData( state );

        if( hasFocus )
        {
            fg = table.getSelectionForeground();
            bg = table.getSelectionBackground();
        }
        else if( isSelected )
        {
            fg = table.getSelectionForeground();
            bg = table.getSelectionBackground();
        }
        else
        {
            fg = table.getForeground();
            bg = table.getBackground();
        }

        rendererCellPanel.setForeground( fg );
        rendererCellPanel.setBackground( bg );

        return rendererCellPanel;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public Component getTableCellEditorComponent( JTable table, Object value,
        boolean isSelected, int row, int column )
    {
        Color bg;
        Color fg;
        state = ( State )value;
        editorCellPanel.setData( state );

        if( isSelected )
        {
            fg = table.getSelectionForeground();
            bg = table.getSelectionBackground();
        }
        else
        {
            fg = table.getSelectionForeground();
            bg = table.getSelectionBackground();
        }

        rendererCellPanel.setForeground( fg );
        rendererCellPanel.setBackground( bg );
        editorCellPanel.setForeground( fg );
        editorCellPanel.setBackground( bg );

        return editorCellPanel;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public Object getCellEditorValue()
    {
        return state;
    }
}

/*******************************************************************************
 * 
 ******************************************************************************/
class TableDataCellPanel extends JPanel
{
    /**  */
    private JLabel stateLabel = new JLabel( "" );

    /**  */
    private JButton removeButton;

    /***************************************************************************
     * @param isEditor
     **************************************************************************/
    public TableDataCellPanel()
    {
        super();
        stateLabel = new JLabel( "" );
        removeButton = new JButton(
            IconConstants.loader.getIcon( IconConstants.EDIT_DELETE_16 ) );

        setLayout( new GridBagLayout() );

        removeButton.setFocusPainted( false );

        add( stateLabel, new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 2, 2,
                2, 2 ), 0, 0 ) );

        add( removeButton, new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(
                2, 2, 2, 2 ), 0, 0 ) );

        add( Box.createHorizontalStrut( 0 ), new GridBagConstraints( 2, 0, 1,
            1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
            new Insets( 2, 2, 2, 2 ), 0, 0 ) );
    }

    /***************************************************************************
     * @param state
     **************************************************************************/
    public void setData( State state )
    {
        if( state != null )
        {
            stateLabel.setText( state.toString() );
            removeButton.setVisible( state == State.NotDone );
        }
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addActionListener( ActionListener l )
    {
        removeButton.addActionListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void setForeground( Color fg )
    {
        super.setForeground( fg );

        // State label may be null when this is called because it is called from
        // the parent's constructor
        if( stateLabel != null )
        {
            stateLabel.setForeground( fg );
        }
    }
}

/*******************************************************************************
 * 
 ******************************************************************************/
class CustomTableModel implements TableModel
{
    /**  */
    public final String[] headings = new String[] { "The String", "The Number",
        "The State" };

    /**  */
    public final Class<?>[] classes = new Class<?>[] { String.class,
        Integer.class, State.class };

    /**  */
    private List<TableData> tableData;

    /***************************************************************************
     * 
     **************************************************************************/
    public CustomTableModel()
    {
        tableData = new ArrayList<TableData>( 100 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public String getColumnName( int col )
    {
        return headings[col];
    }

    /***************************************************************************
     * @param data
     **************************************************************************/
    public void addData( TableData data )
    {
        tableData.add( data );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void clear()
    {
        tableData.clear();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public int getColumnCount()
    {
        return headings.length;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public int getRowCount()
    {
        return tableData != null ? tableData.size() : 0;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public Object getValueAt( int row, int col )
    {
        TableData data = tableData.get( row );
        switch( col )
        {
            case 0:
                return data.text;
            case 1:
                return data.num;
            case 2:
                return data.state;
        }
        return null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void setValueAt( Object obj, int row, int col )
    {
        TableData data = tableData.get( row );
        switch( col )
        {
            case 0:
                data.text = obj.toString();
                break;
            case 1:
                data.num = ( Integer )obj;
                break;
            case 2:
                data.state = ( State )obj;
                break;
        }

        throw new IllegalArgumentException( "Invalid column " + col );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public Class<?> getColumnClass( int col )
    {
        return classes[col];
    }

    public void addTableModelListener( TableModelListener l )
    {
        ;
    }

    public boolean isCellEditable( int row, int col )
    {
        return col == 2;
    }

    public void removeTableModelListener( TableModelListener l )
    {
        ;
    }
}
