package org.cojo.ui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import org.cojo.model.IChangeRequest;
import org.cojo.model.ISoftwareTask;
import org.cojo.ui.tableModels.StfTableModel;
import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.ui.model.IView;
import org.jutils.ui.model.ItemsTableModel;

/***************************************************************************
 * 
 **************************************************************************/
public class StfsPanel implements IView<JPanel>
{
    /**  */
    private final JPanel view;
    /**  */
    private final ItemsTableModel<ISoftwareTask> stfTableModel;
    /**  */
    private final JTable stfTable;

    /***************************************************************************
     * 
     **************************************************************************/
    public StfsPanel()
    {
        this.view = new JPanel( new BorderLayout() );
        this.stfTableModel = new ItemsTableModel<>( new StfTableModel() );
        this.stfTable = new JTable( stfTableModel );

        view.add( createToolbar(), BorderLayout.NORTH );
        view.add( createMainPanel(), BorderLayout.CENTER );
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
        Frame frame = SwingUtils.getComponentsFrame( view );
        StfPanel stfPanel = new StfPanel();
        JEditDialog stfDialog = new JEditDialog( frame, stfPanel.getView() );

        int row = stfTable.getSelectedRow();
        row = stfTable.convertRowIndexToModel( row );
        if( row > -1 )
        {
            ISoftwareTask task = stfTableModel.getItem( row );

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
    private JPanel createMainPanel()
    {
        JPanel mainPanel = new JPanel( new BorderLayout() );
        JScrollPane scrollPane = new JScrollPane( stfTable );

        stfTable.setAutoResizeMode( JTable.AUTO_RESIZE_LAST_COLUMN );
        stfTable.getColumnModel().getColumn( 0 ).setPreferredWidth( 50 );
        stfTable.getColumnModel().getColumn( 1 ).setPreferredWidth( 500 );
        stfTable.getColumnModel().getColumn( 2 ).setPreferredWidth( 100 );
        stfTable.addMouseListener( new MouseAdapter()
        {
            @Override
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
    private static JToolBar createToolbar()
    {
        JToolBar toolbar = new JToolBar();

        JButton addButton = new JButton();
        JButton deleteButton = new JButton();

        addButton.setIcon( IconConstants.getIcon( IconConstants.EDIT_ADD_16 ) );
        addButton.setToolTipText( "Add an STF" );
        addButton.setFocusable( false );

        deleteButton.setIcon(
            IconConstants.getIcon( IconConstants.EDIT_DELETE_16 ) );
        deleteButton.setToolTipText( "Delete an STF" );
        deleteButton.setFocusable( false );

        toolbar.add( addButton );
        toolbar.add( deleteButton );

        toolbar.setFloatable( false );
        toolbar.setRollover( true );
        toolbar.setBorderPainted( false );

        return toolbar;
    }

    @Override
    public JPanel getView()
    {
        return view;
    }
}
