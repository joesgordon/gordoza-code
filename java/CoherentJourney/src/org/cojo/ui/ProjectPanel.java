package org.cojo.ui;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import org.cojo.data.ProjectManager;
import org.jutils.ui.model.IDataView;

/*******************************************************************************
 *
 ******************************************************************************/
public class ProjectPanel implements IDataView<ProjectManager>
{
    /**  */
    private final JComponent view;
    /**  */
    private final UsersPanel usersPanel;
    /**  */
    private final TasksPanel tasksPanel;

    /**  */
    private ProjectManager project;

    /***************************************************************************
     * 
     **************************************************************************/
    public ProjectPanel()
    {
        this.usersPanel = new UsersPanel();
        this.tasksPanel = new TasksPanel();
        this.view = createView();

        setData( new ProjectManager() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JComponent createView()
    {
        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab( "Tasks", tasksPanel.getView() );
        tabs.addTab( "Users", usersPanel.getView() );

        return tabs;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public ProjectManager getData()
    {
        return project;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( ProjectManager proj )
    {
        this.project = proj;

        usersPanel.setProject( project );
        tasksPanel.setProject( proj );

        usersPanel.setData( project.project.users );
    }
}
