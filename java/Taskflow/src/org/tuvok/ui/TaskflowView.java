package org.tuvok.ui;

import java.awt.*;
import java.util.Vector;

import javax.swing.*;

import org.jutils.ui.model.IView;
import org.tuvok.data.Project;

/*******************************************************************************
 * Displays all the main components of the 2DLs interface.
 ******************************************************************************/
public class TaskflowView implements IView<JPanel>
{
    // -------------------------------------------------------------------------
    // GUI Components.
    // -------------------------------------------------------------------------
    /**  */
    private final JPanel view;
    /** The split pane used to divide the tree view from the content pane. */
    private final JSplitPane mainSplitPane = new JSplitPane();
    /** The tree contains all the open projects. */
    private final JList<String> tree = new JList<>();
    // --------------------------------------------------------------------------
    // Supporting data shown in components.
    // --------------------------------------------------------------------------
    /** Vector of project currently displayed in this panel */
    private Vector<Project> projects = new Vector<Project>();

    /***************************************************************************
     * Creates a new panel.
     **************************************************************************/
    public TaskflowView()
    {
        this.view = new JPanel( new GridBagLayout() );

        JScrollPane treeScrollPane = new JScrollPane( tree );
        JScrollPane rightScrollPane = new JScrollPane();

        treeScrollPane.setMinimumSize( new Dimension( 200, 200 ) );
        rightScrollPane.setMinimumSize( new Dimension( 300, 300 ) );

        mainSplitPane.setOrientation( JSplitPane.HORIZONTAL_SPLIT );
        mainSplitPane.setTopComponent( treeScrollPane );
        mainSplitPane.setBottomComponent( rightScrollPane );
        mainSplitPane.setOneTouchExpandable( true );
        // mainSplitPane.setDividerLocation( 200 );

        view.add( mainSplitPane,
            new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );
    }

    /***************************************************************************
     * @param proj Project
     * @return boolean
     **************************************************************************/
    public boolean addProject( Project proj )
    {
        if( !containsProject( proj ) )
        {
            return projects.add( proj );
        }
        return false;
    }

    /***************************************************************************
     * @param proj Project
     * @return boolean
     **************************************************************************/
    public boolean containsProject( Project proj )
    {
        return projects.contains( proj );
    }

    /***************************************************************************
     * @param proj Project
     * @return boolean
     **************************************************************************/
    public boolean removeProject( Project proj )
    {
        return projects.remove( proj );
    }

    /***************************************************************************
     * @return Vector
     **************************************************************************/
    public Vector<Project> getProjects()
    {
        return new Vector<Project>( this.projects );
    }

    /***************************************************************************
     *
     **************************************************************************/
    public void removeAllProjects()
    {
        projects.removeAllElements();
    }

    /***************************************************************************
     *
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }
}
