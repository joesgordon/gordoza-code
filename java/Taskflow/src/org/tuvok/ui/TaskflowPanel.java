package org.tuvok.ui;

import java.awt.*;
import java.util.Vector;

import javax.swing.*;

import org.jutils.ui.model.IView;
import org.tuvok.data.Project;

/*******************************************************************************
 * Displays all the main components of the 2DLs interface.
 ******************************************************************************/
public class TaskflowPanel implements IView<JPanel>
{
    // -------------------------------------------------------------------------
    // GUI Components.
    // -------------------------------------------------------------------------
    /**  */
    private final JPanel view;
    /** The split pane used to divide the tree view from the content pane. */
    private final JSplitPane mainSplitPane = new JSplitPane();
    /** Scroll pane used to allow the tree to scroll. */
    private final JScrollPane treeScrollPane = new JScrollPane();
    /** Scroll pane used to allow the content pane to scroll. */
    private final JScrollPane rightScrollPane = new JScrollPane();
    /** The tree contains all the open projects. */
    private final JList<String> tree = new JList<>();
    /** Displays the context of the currently viewed item. */
    private final JTextField addressTextField = new JTextField();
    // --------------------------------------------------------------------------
    // Supporting data shown in components.
    // --------------------------------------------------------------------------
    /** Vector of project currently displayed in this panel */
    private Vector<Project> projects = new Vector<Project>();

    /***************************************************************************
     * Creates a new panel.
     **************************************************************************/
    public TaskflowPanel()
    {
        this.view = new JPanel( new GridBagLayout() );

        addressTextField.setText( "" );
        addressTextField.setBorder( BorderFactory.createLoweredBevelBorder() );
        addressTextField.setEditable( false );

        treeScrollPane.setMinimumSize( new Dimension( 200, 200 ) );
        treeScrollPane.setViewportView( tree );
        rightScrollPane.setMinimumSize( new Dimension( 300, 300 ) );

        mainSplitPane.setOrientation( JSplitPane.VERTICAL_SPLIT );
        mainSplitPane.setTopComponent( treeScrollPane );
        mainSplitPane.setBottomComponent( rightScrollPane );
        mainSplitPane.setOneTouchExpandable( true );
        // mainSplitPane.setDividerLocation( 200 );

        view.add( addressTextField,
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );

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
