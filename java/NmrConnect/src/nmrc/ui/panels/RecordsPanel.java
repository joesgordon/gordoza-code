package nmrc.ui.panels;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.*;

import nmrc.model.INmrData;
import nmrc.model.IPeak;
import nmrc.ui.tables.ItemTable;
import nmrc.ui.tables.models.PeakRecordTableModel;

import org.jutils.IconConstants;
import org.jutils.ui.event.ItemActionList;
import org.jutils.ui.event.ItemActionListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class RecordsPanel extends JPanel
{
    /**  */
    private ItemTable<IPeak> recordsTable;
    /**  */
    private JButton findDuplicatesButton;
    /**  */
    private JButton assignDuplicateButton;
    /**  */
    private JButton removeDuplicateButton;

    /**  */
    private INmrData nmrData;
    /**  */
    private ItemActionList<INmrData> findDuplicatesListeners;
    /**  */
    private ItemActionList<IPeak> assignDuplicatesListeners;
    /**  */
    private ItemActionList<IPeak> removeDuplicatesListeners;

    /***************************************************************************
     * 
     **************************************************************************/
    public RecordsPanel()
    {
        super();

        findDuplicatesListeners = new ItemActionList<INmrData>();
        assignDuplicatesListeners = new ItemActionList<IPeak>();
        removeDuplicatesListeners = new ItemActionList<IPeak>();

        ActionListener findDuplicatesListener = new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                findDuplicatesListeners.fireListeners( this, nmrData );
            }
        };

        ActionListener removeDuplicatesListener = new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                int row = recordsTable.getSelectedRow();

                if( row > -1 )
                {
                    row = recordsTable.convertRowIndexToModel( row );
                    removeDuplicatesListeners.fireListeners( this,
                        recordsTable.getTableModel().getRow( row ) );
                }
            }
        };

        ActionListener assignDuplicatesListener = new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                int row = recordsTable.getSelectedRow();

                if( row > -1 )
                {
                    row = recordsTable.convertRowIndexToModel( row );
                    assignDuplicatesListeners.fireListeners( this,
                        recordsTable.getTableModel().getRow( row ) );
                }
            }
        };

        // ---------------------------------------------------------------------
        // 
        // ---------------------------------------------------------------------
        JToolBar toolbar = new JToolBar();

        findDuplicatesButton = new JButton(
            IconConstants.getIcon( IconConstants.CHECK_16 ) );
        assignDuplicateButton = new JButton(
            IconConstants.getIcon( IconConstants.EDIT_ADD_16 ) );
        removeDuplicateButton = new JButton(
            IconConstants.getIcon( IconConstants.EDIT_DELETE_16 ) );

        findDuplicatesButton.setFocusable( false );
        findDuplicatesButton.setEnabled( false );
        findDuplicatesButton.setToolTipText( "Check for duplicate peaks" );
        findDuplicatesButton.addActionListener( findDuplicatesListener );

        assignDuplicateButton.setFocusable( false );
        assignDuplicateButton.setEnabled( false );
        assignDuplicateButton.setToolTipText( "Assign duplicate peak(s)" );
        assignDuplicateButton.addActionListener( assignDuplicatesListener );

        removeDuplicateButton.setFocusable( false );
        removeDuplicateButton.setEnabled( false );
        removeDuplicateButton.setToolTipText( "Remove duplicate peak(s)" );
        removeDuplicateButton.addActionListener( removeDuplicatesListener );

        toolbar.add( findDuplicatesButton );
        toolbar.add( assignDuplicateButton );
        toolbar.add( removeDuplicateButton );

        toolbar.setFloatable( false );
        toolbar.setRollover( true );
        toolbar.setBorderPainted( false );

        // ---------------------------------------------------------------------
        // Setup records scroll pane.
        // ---------------------------------------------------------------------
        recordsTable = new ItemTable<IPeak>( new PeakRecordTableModel() );
        JScrollPane recordsScrollPane = new JScrollPane( recordsTable );

        recordsTable.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );

        // ---------------------------------------------------------------------
        // 
        // ---------------------------------------------------------------------
        setLayout( new BorderLayout() );

        add( toolbar, BorderLayout.NORTH );
        add( recordsScrollPane, BorderLayout.CENTER );
    }

    /***************************************************************************
     * @param records
     **************************************************************************/
    public void setData( INmrData data )
    {
        nmrData = data;

        recordsTable.getTableModel().setItems( data.getPeaks() );
        recordsTable.getTableModel().setColumnNames(
            Arrays.asList( PeakRecordTableModel.COL_NAMES ) );

        findDuplicatesButton.setEnabled( true );
        assignDuplicateButton.setEnabled( true );
        removeDuplicateButton.setEnabled( true );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public INmrData getData()
    {
        return nmrData;
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addFindDuplicatesListener( ItemActionListener<INmrData> l )
    {
        findDuplicatesListeners.addListener( l );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addAssignDuplicatesListener( ItemActionListener<IPeak> l )
    {
        assignDuplicatesListeners.addListener( l );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addRemoveDuplicatesListener( ItemActionListener<IPeak> l )
    {
        removeDuplicatesListeners.addListener( l );
    }
}
