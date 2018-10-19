package org.taskflow.ui;

import java.awt.*;

import javax.swing.*;

import org.jutils.ui.model.IDataView;
import org.taskflow.data.Project;

/*******************************************************************************
 * Displays all the main components of the 2DLs interface.
 ******************************************************************************/
public class TaskflowView implements IDataView<Project>
{
    /**  */
    private final JPanel view;
    /** The tree contains all the open projects. */
    private final JList<String> tree = new JList<>();

    /** Project currently displayed in this panel */
    private Project project;

    /***************************************************************************
     * Creates a new panel.
     **************************************************************************/
    public TaskflowView()
    {
        this.view = new JPanel( new GridBagLayout() );
        this.project = new Project();

        JScrollPane treeScrollPane = new JScrollPane( tree );
        JScrollPane rightScrollPane = new JScrollPane();

        treeScrollPane.setMinimumSize( new Dimension( 200, 200 ) );
        rightScrollPane.setMinimumSize( new Dimension( 300, 300 ) );

        JSplitPane mainSplitPane = new JSplitPane();

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
     * @param project
     **************************************************************************/
    public void setData( Project project )
    {
        this.project = project;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Project getData()
    {
        return this.project;
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
