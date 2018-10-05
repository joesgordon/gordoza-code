package org.cojo.ui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import org.cojo.data.*;
import org.jutils.ui.model.IDataView;

/*******************************************************************************
 *
 ******************************************************************************/
public class CojoPanel implements IDataView<Project>
{
    /**  */
    private final JPanel view;
    /**  */
    private final CrsPanel crsPanel;
    /**  */
    private final CrPanel crPanel;

    /**  */
    private Project project;

    /***************************************************************************
     * 
     **************************************************************************/
    public CojoPanel()
    {
        this.crsPanel = new CrsPanel();
        this.crPanel = new CrPanel();
        this.view = createView();

        setDefaultData();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel mainPanel = new JPanel( new BorderLayout() );
        JSplitPane mainSplitPane = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT );

        mainSplitPane.setTopComponent( crsPanel.getView() );
        mainSplitPane.setBottomComponent( crPanel.getView() );
        mainSplitPane.validate();
        mainSplitPane.setResizeWeight( 0.5 );
        mainSplitPane.setDividerLocation( 0.9 );
        mainSplitPane.setOneTouchExpandable( true );

        mainPanel.add( mainSplitPane, BorderLayout.CENTER );

        return mainPanel;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void setDefaultData()
    {
        Project proj = new Project();

        User u1 = proj.createUser( "Arnold Rimmer" );
        User u2 = proj.createUser( "Dave Lister" );

        ChangeRequest cr = createDefaultCr( u1, u2 );
        proj.changes.add( cr );

        setData( proj );
    }

    /***************************************************************************
     * @param u2
     * @param u1
     * @return
     **************************************************************************/
    private static ChangeRequest createDefaultCr( User u1, User u2 )
    {
        ChangeRequest cr = new ChangeRequest( 154, u1.userId );
        List<SoftwareTask> tasks = new ArrayList<SoftwareTask>();

        String designComment = "Don't you think that a little more design needs to be done than, \"Not like a fish?\"";

        List<Finding> findings = new ArrayList<Finding>();
        findings.add( new Finding( 1, u1.userId, "Not like a fish.", false,
            designComment ) );

        cr.title = "Update the Software to be Less Atrocious";
        cr.description = "Doesn't work right";
        cr.state = CrState.IN_WORK;
        cr.reviews.addAll( findings );

        tasks.add(
            new SoftwareTask( 1, "Make stuff work", u2.userId, 1, 6, false,
                "Do the quick fix", "Push the button", "Get banana", null ) );
        tasks.add( new SoftwareTask( 2, "Make Arnold's stuff work correctly",
            u1.userId, 200, 66, true, "Fix Rimmer's \"quick\" fix",
            "Push the button", "watch it work", null ) );

        cr.tasks.addAll( tasks );

        return cr;
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
    public Project getData()
    {
        return project;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setData( Project proj )
    {
        this.project = proj;

        crsPanel.setProject( project );
        crPanel.setProject( project );

        crsPanel.setData( project.changes );
        if( project.changes.isEmpty() )
        {
            crsPanel.clear();
        }
        else
        {
            crPanel.setData( project.changes.get( 0 ) );
        }
    }
}
