package org.cojo.ui;

import java.awt.BorderLayout;
import java.util.*;

import javax.swing.*;

import org.cojo.data.*;
import org.cojo.model.*;
import org.jutils.IconConstants;
import org.jutils.ui.JGoodiesToolBar;
import org.jutils.ui.StandardFrameView;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class CojoFrame implements IView<JFrame>
{
    private final StandardFrameView frameView;
    private final CrsPanel crsPanel;
    private final CrPanel crPanel;

    /***************************************************************************
     * 
     **************************************************************************/
    public CojoFrame()
    {
        this.frameView = new StandardFrameView();
        this.crsPanel = new CrsPanel();
        this.crPanel = new CrPanel();

        frameView.setToolbar( createToolBar() );
        frameView.setContent( createMainPanel() );

        frameView.setTitle( "CoherentJourney v1.0" );
        frameView.setSize( 800, 600 );

        setDefaultData();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createMainPanel()
    {
        JPanel mainPanel = new JPanel( new BorderLayout() );
        JSplitPane mainSplitPane = new JSplitPane( JSplitPane.VERTICAL_SPLIT );

        mainSplitPane.setTopComponent( crsPanel.getView() );
        mainSplitPane.setBottomComponent( crPanel.getView() );
        mainSplitPane.validate();
        mainSplitPane.setDividerLocation( 100 );

        mainPanel.add( mainSplitPane, BorderLayout.CENTER );

        return mainPanel;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void setDefaultData()
    {
        IChangeRequest cr = createDefaultCr();
        List<IChangeRequest> crs = new ArrayList<IChangeRequest>();
        crs.add( cr );

        crsPanel.setData( crs );
        crPanel.setData( cr );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static ChangeRequest createDefaultCr()
    {
        ChangeRequest cr = new ChangeRequest( 154 );
        List<ISoftwareTask> tasks = new ArrayList<ISoftwareTask>();

        IUser ajRimmer = new User( "ajrimmer", "Arnold Rimmer" );
        IUser dmLister = new User( "dmlister", "Dave Lister" );

        String designComment = "Don't you think that a little more design needs to be done than, \"Not like a fish?\"";

        List<IFinding> findings = new ArrayList<IFinding>();
        findings.add( new Finding( 1, dmLister, ( new Date() ).getTime(),
            "Not like a fish.", false, designComment ) );

        cr.setTitle( "Update the Software to be Less Atrocious" );
        cr.setDescription( "Doesn't work right" );
        cr.setImpact( "The bit that's supposed to work" );
        cr.setRationale( "Because it doesn't work right" );
        cr.setState( CrState.InWork );
        cr.setDesignReviews( findings );

        tasks.add(
            new SoftwareTask( 1, "Make stuff work", ajRimmer, 1, 6, false,
                "Do the quick fix", "Push the button", "Get banana", null ) );
        tasks.add( new SoftwareTask( 2, "Make Arnold's stuff work correctly",
            dmLister, 200, 66, true, "Fix Rimmer's \"quick\" fix",
            "Push the button", "watch it work", null ) );

        cr.setTasks( tasks );

        return cr;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static JToolBar createToolBar()
    {
        JToolBar toolbar = new JGoodiesToolBar();

        JButton newButton = new JButton();
        JButton openButton = new JButton();
        JButton saveButton = new JButton();

        newButton.setIcon(
            IconConstants.loader.getIcon( IconConstants.NEW_FILE_16 ) );
        newButton.setFocusable( false );

        openButton.setIcon(
            IconConstants.loader.getIcon( IconConstants.OPEN_FOLDER_16 ) );
        openButton.setFocusable( false );

        saveButton.setIcon(
            IconConstants.loader.getIcon( IconConstants.SAVE_16 ) );
        saveButton.setFocusable( false );

        toolbar.add( newButton );
        toolbar.add( openButton );
        toolbar.add( saveButton );

        toolbar.setFloatable( false );
        toolbar.setRollover( true );
        toolbar.setBorderPainted( false );

        return toolbar;
    }

    @Override
    public JFrame getView()
    {
        return frameView.getView();
    }
}
