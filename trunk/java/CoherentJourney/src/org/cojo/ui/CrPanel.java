package org.cojo.ui;

import java.awt.*;

import javax.swing.*;

import org.cojo.CojoIconLoader;
import org.cojo.model.IChangeRequest;
import org.jutils.IconConstants;
import org.jutils.ui.event.ItemActionEvent;
import org.jutils.ui.event.ItemActionListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class CrPanel extends JPanel
{
    private JTabbedPane mainTabbedPane;

    private JLabel numberLabel;
    private JTextField titleField;
    private JTextField stateField;

    private CrDefinitionPanel crDefinitionPanel;
    private StfsPanel stfsPanel;
    private NotesPanel notesPanel;

    private JPanel thirdPartyPanel;
    private JPanel systemEngineeringPanel;
    private FindingsPanel designPanel;

    /***************************************************************************
     * 
     **************************************************************************/
    public CrPanel()
    {
        super( new BorderLayout() );

        add( createToolbar(), BorderLayout.NORTH );
        add( createMainPanel(), BorderLayout.CENTER );
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
    private JToolBar createToolbar()
    {
        JToolBar toolbar = new JToolBar();

        JButton saveButton = new JButton();
        JButton printButton = new JButton();
        JButton stateButton = new JButton();
        JButton helpButton = new JButton();

        saveButton.setIcon( IconConstants.loader.getIcon( IconConstants.SAVE_16 ) );
        saveButton.setToolTipText( "Save CR" );
        saveButton.setFocusable( false );

        printButton.setIcon( CojoIconLoader.getloader().getIcon(
            CojoIconLoader.PRINT_16 ) );
        printButton.setToolTipText( "Print CR" );
        printButton.setFocusable( false );

        stateButton.setIcon( CojoIconLoader.getloader().getIcon(
            CojoIconLoader.SWITCH_16 ) );
        stateButton.setToolTipText( "Change State" );
        stateButton.setFocusable( false );

        helpButton.setIcon( CojoIconLoader.getloader().getIcon(
            CojoIconLoader.HELP_16 ) );
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

        crDefinitionPanel = new CrDefinitionPanel();
        stfsPanel = new StfsPanel();
        notesPanel = new NotesPanel();
        designPanel = new FindingsPanel();
        systemEngineeringPanel = new JPanel();
        thirdPartyPanel = new JPanel();

        mainTabbedPane = new JTabbedPane();
        JScrollPane crScrollPane = new JScrollPane( crDefinitionPanel );

        crScrollPane.setBorder( null );
        crScrollPane.getVerticalScrollBar().setUnitIncrement( 9 );

        crDefinitionPanel.addThirdPartyIpListener( thirdPartyListener );
        crDefinitionPanel.addRequirementImpactListener( requirementsImpactListener );

        mainTabbedPane.addTab( "Change Request", crScrollPane );
        mainTabbedPane.addTab( "STFs", stfsPanel );
        mainTabbedPane.addTab( "Notes", notesPanel );
        mainTabbedPane.addTab( "Design", designPanel );

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
        numberLabel = new JLabel( "CR # " );
        titleField = new JTextField( "CR Title" );
        stateField = new JTextField( "State", 20 );

        titleField.setToolTipText( "CR Title" );
        stateField.setEditable( false );
        stateField.setHorizontalAlignment( JTextField.CENTER );
        stateField.setToolTipText( "CR State" );

        topPanel.add( numberLabel, new GridBagConstraints( 0, 0, 1, 1, 0.0,
            0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                4, 4, 2, 2 ), 0, 0 ) );
        topPanel.add( titleField, new GridBagConstraints( 1, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(
                4, 2, 4, 2 ), 0, 0 ) );
        topPanel.add( stateField, new GridBagConstraints( 2, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 4, 2,
                4, 4 ), 0, 0 ) );

        return topPanel;
    }
}
