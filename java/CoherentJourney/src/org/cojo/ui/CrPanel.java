package org.cojo.ui;

import java.awt.*;

import javax.swing.*;

import org.cojo.CojoIconLoader;
import org.cojo.model.IChangeRequest;
import org.jutils.IconConstants;
import org.jutils.ui.event.ItemActionEvent;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class CrPanel implements IView<JPanel>
{
    private final JPanel view;
    private final JTabbedPane mainTabbedPane;

    private final JLabel numberLabel;
    private final JTextField titleField;
    private final JTextField stateField;

    private final CrDefinitionPanel crDefinitionPanel;
    private final StfsPanel stfsPanel;
    private final NotesPanel notesPanel;

    private final JPanel thirdPartyPanel;
    private final JPanel systemEngineeringPanel;
    private final FindingsPanel designPanel;

    /***************************************************************************
     * 
     **************************************************************************/
    public CrPanel()
    {
        this.view = new JPanel( new BorderLayout() );
        this.numberLabel = new JLabel( "CR # " );
        this.titleField = new JTextField( "CR Title" );
        this.stateField = new JTextField( "State", 20 );
        this.crDefinitionPanel = new CrDefinitionPanel();
        this.stfsPanel = new StfsPanel();
        this.notesPanel = new NotesPanel();
        this.designPanel = new FindingsPanel();
        this.systemEngineeringPanel = new JPanel();
        this.thirdPartyPanel = new JPanel();
        this.mainTabbedPane = new JTabbedPane();

        view.add( createToolbar(), BorderLayout.NORTH );
        view.add( createMainPanel(), BorderLayout.CENTER );
    }

    /***************************************************************************
     * @param changeRequest
     **************************************************************************/
    public void setData( IChangeRequest cr )
    {
        numberLabel.setText( "CR # " + cr.getNumber() );
        titleField.setText( cr.getTitle() );
        stateField.setText( cr.getState().toString() );

        crDefinitionPanel.setData( cr );
        stfsPanel.setData( cr );
        notesPanel.setData( cr );
        designPanel.setData( cr.getDesignReviews() );
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
            CojoIconLoader.getloader().getIcon( CojoIconLoader.PRINT_16 ) );
        printButton.setToolTipText( "Print CR" );
        printButton.setFocusable( false );

        stateButton.setIcon(
            CojoIconLoader.getloader().getIcon( CojoIconLoader.SWITCH_16 ) );
        stateButton.setToolTipText( "Change State" );
        stateButton.setFocusable( false );

        helpButton.setIcon(
            CojoIconLoader.getloader().getIcon( CojoIconLoader.HELP_16 ) );
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
        mainTabbedPane.addTab( "STFs", stfsPanel.getView() );
        mainTabbedPane.addTab( "Notes", notesPanel.getView() );
        mainTabbedPane.addTab( "Design", designPanel.getView() );

        systemEngineeringPanel.setVisible( false );

        mainPanel.add( createTopPanel(), BorderLayout.NORTH );
        mainPanel.add( mainTabbedPane, BorderLayout.CENTER );

        return mainPanel;
    }

    /**
     * @param show
     */
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

    /**
     * @param show
     */
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
     * 
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }
}
