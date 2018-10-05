package org.cojo.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;

import org.cojo.data.ChangeRequest;
import org.cojo.data.Project;
import org.cojo.ui.tableModels.CrTableConfig;
import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.ui.event.ResizingTableModelListener;
import org.jutils.ui.model.IView;
import org.jutils.ui.model.ItemsTableModel;

/*******************************************************************************
 * 
 ******************************************************************************/
public class CrsPanel implements IView<JPanel>
{
    /**  */
    private final JPanel view;
    /**  */
    private final CrTableConfig tableConfig;
    /**  */
    private final ItemsTableModel<ChangeRequest> tableModel;
    /**  */
    private final JTable table;

    /***************************************************************************
     * 
     **************************************************************************/
    public CrsPanel()
    {
        this.view = new JPanel( new BorderLayout() );
        this.tableConfig = new CrTableConfig();
        this.tableModel = new ItemsTableModel<>( tableConfig );
        this.table = new JTable( tableModel );

        view.add( createToolbar(), BorderLayout.NORTH );
        view.add( createMainPanel(), BorderLayout.CENTER );

        table.setFont( table.getFont().deriveFont( 14.0f ) );
        table.setRowHeight( table.getRowHeight() + 6 );
        table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
    }

    /***************************************************************************
     * @param crs
     **************************************************************************/
    public void setData( List<ChangeRequest> crs )
    {
        tableModel.setItems( crs );

        if( crs != null && !crs.isEmpty() )
        {
            table.getSelectionModel().setSelectionInterval( 0, 0 );
        }

        ResizingTableModelListener.resizeTable( table );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createMainPanel()
    {
        JPanel mainPanel = new JPanel( new BorderLayout() );
        JScrollPane crsScrollPane = new JScrollPane( table );

        table.setAutoResizeMode( JTable.AUTO_RESIZE_LAST_COLUMN );
        table.getColumnModel().getColumn( 0 ).setPreferredWidth( 50 );
        table.getColumnModel().getColumn( 1 ).setPreferredWidth( 500 );
        table.getColumnModel().getColumn( 2 ).setPreferredWidth( 100 );

        mainPanel.add( crsScrollPane, BorderLayout.CENTER );

        return mainPanel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JToolBar createToolbar()
    {
        JToolBar toolbar = new JToolBar();

        SwingUtils.setToolbarDefaults( toolbar );

        SwingUtils.addActionToToolbar( toolbar, createAddAction() );

        return toolbar;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createAddAction()
    {
        ActionListener listener = ( e ) -> addNewChangeRequest();
        Icon icon = IconConstants.getIcon( IconConstants.EDIT_ADD_16 );
        return new ActionAdapter( listener, "Add a CR", icon );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void addNewChangeRequest()
    {
        // TODO Auto-generated method stub
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
     * 
     **************************************************************************/
    public void clear()
    {
        // TODO Auto-generated method stub
    }

    /***************************************************************************
     * @param project
     **************************************************************************/
    public void setProject( Project project )
    {
        tableConfig.setProject( project );
    }
}
