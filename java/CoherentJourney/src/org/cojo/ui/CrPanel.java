package org.cojo.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import org.cojo.CojoIcons;
import org.cojo.data.Project;
import org.cojo.data.Task;
import org.jutils.core.IconConstants;
import org.jutils.core.ui.event.ItemActionEvent;
import org.jutils.core.ui.event.ItemActionListener;
import org.jutils.core.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class CrPanel implements IDataView<Task>
{
    /**  */
    private final JPanel view;
    /**  */
    private final JTabbedPane mainTabbedPane;

    /**  */
    private final JLabel numberLabel;
    /**  */
    private final JTextField titleField;
    /**  */
    private final JTextField stateField;

    /**  */
    private final TaskDefinitionPanel crDefinitionPanel;
    /**  */
    private final NotesPanel notesPanel;

    /**  */
    private final JPanel thirdPartyPanel;
    /**  */
    private final JPanel systemEngineeringPanel;
    /**  */
    private final FindingsPanel designPanel;
    /**  */
    private Task task;

    /***************************************************************************
     * 
     **************************************************************************/
    public CrPanel()
    {
        this.view = new JPanel( new BorderLayout() );
        this.numberLabel = new JLabel( "CR # " );
        this.titleField = new JTextField( "CR Title" );
        this.stateField = new JTextField( "State", 20 );
        this.crDefinitionPanel = new TaskDefinitionPanel();
        this.notesPanel = new NotesPanel();
        this.designPanel = new FindingsPanel();
        this.systemEngineeringPanel = new JPanel();
        this.thirdPartyPanel = new JPanel();
        this.mainTabbedPane = new JTabbedPane();

        view.add( createToolbar(), BorderLayout.NORTH );
        view.add( createMainPanel(), BorderLayout.CENTER );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Task getData()
    {
        return task;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    public void setData( Task changeRequest )
    {
        this.task = changeRequest;

        numberLabel.setText( "CR # " + changeRequest.id );
        titleField.setText( changeRequest.title );
        stateField.setText( changeRequest.state.toString() );

        crDefinitionPanel.setData( changeRequest );
        notesPanel.setData( changeRequest );
        designPanel.setData( changeRequest.codeReviews );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static JToolBar createToolbar()
    {
        JToolBar toolbar = new JToolBar();

        JButton saveButton = new JButton();
        JButton printButton = new JButton();
        JButton stateButton = new JButton();
        JButton helpButton = new JButton();

        saveButton.setIcon( IconConstants.getIcon( IconConstants.SAVE_16 ) );
        saveButton.setToolTipText( "Save CR" );
        saveButton.setFocusable( false );

        printButton.setIcon(
            CojoIcons.getloader().getIcon( CojoIcons.PRINT_16 ) );
        printButton.setToolTipText( "Print CR" );
        printButton.setFocusable( false );

        stateButton.setIcon(
            CojoIcons.getloader().getIcon( CojoIcons.SWITCH_16 ) );
        stateButton.setToolTipText( "Change State" );
        stateButton.setFocusable( false );

        helpButton.setIcon(
            CojoIcons.getloader().getIcon( CojoIcons.HELP_16 ) );
        helpButton.setToolTipText( "Get help" );
        helpButton.setFocusable( false );

        toolbar.add( saveButton );
        toolbar.add( printButton );
        toolbar.add( stateButton );
        toolbar.add( Box.createGlue() );
        toolbar.add( helpButton );

        toolbar.setFloatable( false );
        toolbar.setRollover( true );
        toolbar.setBorderPainted( false );

        return toolbar;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createMainPanel()
    {
        JPanel mainPanel = new JPanel( new BorderLayout() );

        ItemActionListener<Boolean> thirdPartyListener = new ItemActionListener<Boolean>()
        {
            @Override
            public void actionPerformed( ItemActionEvent<Boolean> event )
            {
                showThirdPartyTab( event.getItem() );
            }
        };

        ItemActionListener<Boolean> requirementsImpactListener = new ItemActionListener<Boolean>()
        {
            @Override
            public void actionPerformed( ItemActionEvent<Boolean> event )
            {
                showSystemEngineeringTab( event.getItem() );
            }
        };

        JScrollPane crScrollPane = new JScrollPane(
            crDefinitionPanel.getView() );

        crScrollPane.setBorder( null );
        crScrollPane.getVerticalScrollBar().setUnitIncrement( 9 );

        crDefinitionPanel.addThirdPartyIpListener( thirdPartyListener );
        crDefinitionPanel.addRequirementImpactListener(
            requirementsImpactListener );

        mainTabbedPane.addTab( "Change Request", crScrollPane );
        mainTabbedPane.addTab( "Notes", notesPanel.getView() );
        mainTabbedPane.addTab( "Design", designPanel.getView() );

        systemEngineeringPanel.setVisible( false );

        mainPanel.add( createTopPanel(), BorderLayout.NORTH );
        mainPanel.add( mainTabbedPane, BorderLayout.CENTER );

        return mainPanel;
    }

    /***************************************************************************
     * @param show
     **************************************************************************/
    private void showSystemEngineeringTab( boolean show )
    {
        int idx = mainTabbedPane.indexOfComponent( systemEngineeringPanel );

        if( show && idx < 0 )
        {
            mainTabbedPane.insertTab( "System Engineering", null,
                systemEngineeringPanel, null, 4 );
        }
        else if( !show && idx > -1 )
        {
            mainTabbedPane.removeTabAt( idx );
        }
    }

    /***************************************************************************
     * @param show
     **************************************************************************/
    private void showThirdPartyTab( boolean show )
    {
        int idx = mainTabbedPane.indexOfComponent( thirdPartyPanel );

        if( show && idx < 0 )
        {
            mainTabbedPane.addTab( "Third Party IP", thirdPartyPanel );
        }
        else if( !show && idx > -1 )
        {
            mainTabbedPane.removeTabAt( idx );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createTopPanel()
    {
        JPanel topPanel = new JPanel( new GridBagLayout() );

        titleField.setToolTipText( "CR Title" );
        stateField.setEditable( false );
        stateField.setHorizontalAlignment( JTextField.CENTER );
        stateField.setToolTipText( "CR State" );

        topPanel.add( numberLabel,
            new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 4, 4, 2, 2 ), 0, 0 ) );
        topPanel.add( titleField,
            new GridBagConstraints( 1, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets( 4, 2, 4, 2 ), 0, 0 ) );
        topPanel.add( stateField,
            new GridBagConstraints( 2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 4, 2, 4, 4 ), 0, 0 ) );

        return topPanel;
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
        notesPanel.setProject( project );
        designPanel.setProject( project );
    }
}
