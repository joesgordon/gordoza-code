package org.cojo.ui;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.Frame;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import org.cojo.data.ProjectManager;
import org.cojo.data.Task;
import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.ui.OkDialogView;
import org.jutils.ui.OkDialogView.OkDialogButtons;
import org.jutils.ui.event.ActionAdapter;
import org.jutils.ui.model.DefaultTableItemsConfig;
import org.jutils.ui.model.IDataView;
import org.jutils.ui.model.ITableItemsConfig;
import org.jutils.ui.model.ItemsTableModel;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TasksPanel implements IDataView<List<Task>>
{
    /**  */
    private final JPanel view;
    /**  */
    private final ITableItemsConfig<Task> tableConfig;
    /**  */
    private final ItemsTableModel<Task> tableModel;
    /**  */
    private final JTable table;

    /**  */
    private List<Task> data;
    /**  */
    private ProjectManager manager;

    /***************************************************************************
     * 
     **************************************************************************/
    public TasksPanel()
    {
        this.view = new JPanel( new BorderLayout() );
        this.tableConfig = createTableConfig();
        this.tableModel = new ItemsTableModel<Task>( tableConfig );
        this.table = new JTable( tableModel );

        view.add( createToolbar(), BorderLayout.NORTH );
        view.add( createMainPanel(), BorderLayout.CENTER );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private ITableItemsConfig<Task> createTableConfig()
    {
        DefaultTableItemsConfig<Task> config = new DefaultTableItemsConfig<>();

        config.addCol( "#", Integer.class, ( t ) -> t.id );
        config.addCol( "Priority", String.class, ( t ) -> t.priority.name );
        config.addCol( "Title", String.class, ( t ) -> t.title );
        config.addCol( "Assigned To", String.class,
            ( t ) -> manager.project.getUser( t.assigneeId ).getName() );
        config.addCol( "State", String.class, ( t ) -> t.state.name );

        return config;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( List<Task> tasks )
    {
        this.data = tasks;

        tableModel.setItems( tasks );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public List<Task> getData()
    {
        return data;
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
     * @param row
     **************************************************************************/
    private void showTaskDialog( int row )
    {
        Frame frame = SwingUtils.getComponentsFrame( view );
        TaskPanel taskPanel = new TaskPanel();
        OkDialogView taskDialog = new OkDialogView( frame, taskPanel.getView(),
            ModalityType.DOCUMENT_MODAL, OkDialogButtons.OK_ONLY );

        row = table.convertRowIndexToModel( row );

        Task task = tableModel.getItem( row );

        taskPanel.setProject( manager.project );
        taskPanel.setData( task );

        taskDialog.show( 400, 400 );
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
        table.getColumnModel().getColumn( 1 ).setPreferredWidth( 100 );
        table.getColumnModel().getColumn( 2 ).setPreferredWidth( 500 );
        table.getColumnModel().getColumn( 3 ).setPreferredWidth( 100 );
        table.addMouseListener( new TasksTableMouseListener( this ) );

        mainPanel.add( scrollPane, BorderLayout.CENTER );

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
        SwingUtils.addActionToToolbar( toolbar, createRemoveAction() );

        return toolbar;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createAddAction()
    {
        ActionListener listener = ( e ) -> invokeAdd();
        Icon icon = IconConstants.getIcon( IconConstants.EDIT_ADD_16 );
        return new ActionAdapter( listener, "Add Task", icon );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createRemoveAction()
    {
        ActionListener listener = ( e ) -> invokeRemove();
        Icon icon = IconConstants.getIcon( IconConstants.EDIT_DELETE_16 );
        return new ActionAdapter( listener, "Remove Task", icon );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void invokeAdd()
    {
        NewTaskView taskView = new NewTaskView( manager );
        OkDialogView dialogView = new OkDialogView( getView(),
            taskView.getView(), ModalityType.DOCUMENT_MODAL,
            OkDialogButtons.OK_CANCEL );

        dialogView.setTitle( "New Task" );

        if( dialogView.show( 500, 400 ) )
        {
            Task task = taskView.getData();

            manager.project.tasks.add( task );

            setProject( manager );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void invokeRemove()
    {
        // TODO Auto-generated method stub
    }

    /***************************************************************************
     * @param manager
     **************************************************************************/
    public void setProject( ProjectManager manager )
    {
        this.manager = manager;

        setData( manager.project.tasks );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class TasksTableMouseListener extends MouseAdapter
    {
        /**  */
        private final TasksPanel view;

        /**
         * @param view
         */
        public TasksTableMouseListener( TasksPanel view )
        {
            this.view = view;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void mouseClicked( MouseEvent e )
        {
            if( e.getClickCount() == 2 &&
                SwingUtilities.isLeftMouseButton( e ) )
            {
                int row = view.table.rowAtPoint( e.getPoint() );
                if( row > -1 )
                {
                    view.showTaskDialog( row );
                }
            }
        }
    }
}
