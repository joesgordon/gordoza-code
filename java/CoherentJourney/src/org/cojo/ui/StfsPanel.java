package org.cojo.ui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import org.cojo.data.*;
import org.cojo.ui.tableModels.StfTableModel;
import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.ui.model.IView;
import org.jutils.ui.model.ItemsTableModel;

/*******************************************************************************
 * 
 ******************************************************************************/
public class StfsPanel implements IView<JPanel>
{
    /**  */
    private final JPanel view;
    /**  */
    private final StfTableModel tableConfig;
    /**  */
    private final ItemsTableModel<SoftwareTask> tableModel;
    /**  */
    private final JTable table;

    /**  */
    private Project project;

    /***************************************************************************
     * 
     **************************************************************************/
    public StfsPanel()
    {
        this.view = new JPanel( new BorderLayout() );
        this.tableConfig = new StfTableModel();
        this.tableModel = new ItemsTableModel<>( tableConfig );
        this.table = new JTable( tableModel );

        view.add( createToolbar(), BorderLayout.NORTH );
        view.add( createMainPanel(), BorderLayout.CENTER );
    }

    /***************************************************************************
     * @param cr
     **************************************************************************/
    public void setData( ChangeRequest cr )
    {
        tableModel.setItems( cr.tasks );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void showStfDialog()
    {
        Frame frame = SwingUtils.getComponentsFrame( view );
        StfPanel stfPanel = new StfPanel();
        JEditDialog stfDialog = new JEditDialog( frame, stfPanel.getView() );

        int row = table.getSelectedRow();
        row = table.convertRowIndexToModel( row );
        if( row > -1 )
        {
            SoftwareTask task = tableModel.getItem( row );

            stfPanel.setProject( project );
            stfPanel.setData( task );

            stfDialog.setSize( 400, 400 );
            stfDialog.validate();
            stfDialog.setLocationRelativeTo( frame );
            stfDialog.setVisible( true );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createMainPanel()
    {
        JPanel mainPanel = new JPanel( new BorderLayout() );
        JScrollPane scrollPane = new JScrollPane( table );

        table.setAutoResizeMode( JTable.AUTO_RESIZE_LAST_COLUMN );
        table.getColumnModel().getColumn( 0 ).setPreferredWidth( 50 );
        table.getColumnModel().getColumn( 1 ).setPreferredWidth( 500 );
        table.getColumnModel().getColumn( 2 ).setPreferredWidth( 100 );
        table.addMouseListener( new MouseAdapter()
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

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }

    /***************************************************************************
     * @param project
     **************************************************************************/
    public void setProject( Project project )
    {
        this.project = project;

        tableConfig.setProject( project );
    }
}
