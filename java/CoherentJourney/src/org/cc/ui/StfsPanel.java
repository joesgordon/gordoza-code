package org.cc.ui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import org.cc.model.IChangeRequest;
import org.cc.model.ISoftwareTask;
import org.cc.ui.tableModels.StfTableModel;
import org.jutils.IconConstants;
import org.jutils.Utils;

/***************************************************************************
 * 
 **************************************************************************/
public class StfsPanel extends JPanel
{
    /**  */
    private StfTableModel stfTableModel;
    private JTable stfTable;

    /***************************************************************************
     * 
     **************************************************************************/
    public StfsPanel()
    {
        super( new BorderLayout() );

        stfTableModel = new StfTableModel();

        add( createToolbar(), BorderLayout.NORTH );
        add( createMainPanel( stfTableModel ), BorderLayout.CENTER );
    }

    /***************************************************************************
     * @param cr
     **************************************************************************/
    public void setData( IChangeRequest cr )
    {
        stfTableModel.setItems( cr.getTasks() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void showStfDialog()
    {
        Frame frame = Utils.getComponentsFrame( this );
        StfPanel stfPanel = new StfPanel();
        JEditDialog stfDialog = new JEditDialog( frame, stfPanel );

        int row = stfTable.getSelectedRow();
        row = stfTable.convertRowIndexToModel( row );
        if( row > -1 )
        {
            ISoftwareTask task = stfTableModel.getRow( row );

            stfPanel.setData( task );

            stfDialog.setSize( 400, 400 );
            stfDialog.validate();
            stfDialog.setLocationRelativeTo( frame );
            stfDialog.setVisible( true );
        }
    }

    /***************************************************************************
     * @param tableModel
     * @return
     **************************************************************************/
    private JPanel createMainPanel( StfTableModel tableModel )
    {
        JPanel mainPanel = new JPanel( new BorderLayout() );
        stfTable = new JTable( tableModel );
        JScrollPane scrollPane = new JScrollPane( stfTable );

        stfTable.setAutoResizeMode( JTable.AUTO_RESIZE_LAST_COLUMN );
        stfTable.getColumnModel().getColumn( 0 ).setPreferredWidth( 50 );
        stfTable.getColumnModel().getColumn( 1 ).setPreferredWidth( 500 );
        stfTable.getColumnModel().getColumn( 2 ).setPreferredWidth( 100 );
        stfTable.addMouseListener( new MouseAdapter()
        {
            public void mouseClicked( MouseEvent e )
            {
                if( e.getClickCount() == 2 )
                {
                    showStfDialog();
                }
            }
        } );

        mainPanel.add( scrollPane, BorderLayout.CENTER );

        return mainPanel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JToolBar createToolbar()
    {
        JToolBar toolbar = new JToolBar();

        JButton addButton = new JButton();
        JButton deleteButton = new JButton();

        addButton.setIcon( IconConstants.getIcon( IconConstants.EDIT_ADD_16 ) );
        addButton.setToolTipText( "Add an STF" );
        addButton.setFocusable( false );

        deleteButton.setIcon( IconConstants.getIcon( IconConstants.EDIT_DELETE_16 ) );
        deleteButton.setToolTipText( "Delete an STF" );
        deleteButton.setFocusable( false );

        toolbar.add( addButton );
        toolbar.add( deleteButton );

        toolbar.setFloatable( false );
        toolbar.setRollover( true );
        toolbar.setBorderPainted( false );

        return toolbar;
    }
}
