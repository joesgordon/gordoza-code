package org.cojo.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import org.cojo.model.IChangeRequest;
import org.jutils.IconConstants;
import org.jutils.ui.event.ItemActionList;
import org.jutils.ui.event.ItemActionListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class CrDefinitionPanel extends JPanel
{
    /**  */
    private JTextField originatorField;
    /**  */
    private JTextField dateCreatedField;
    /**  */
    private JTextField priorityField;
    /**  */
    private JTextField areaField;
    /**  */
    private JCheckBox thirdPartyField;
    /**  */
    private JCheckBox requirementsImpactField;
    /**  */
    private JCheckBox designField;
    /**  */
    private JTextField hwciField;
    /**  */
    private JTextField crTypeField;
    /**  */
    private JTextField phaseDiscoveredField;
    /**  */
    private JTextField phaseOriginatedField;
    /**  */
    private JTextField estHoursField;

    /**  */
    private JTextField versionDiscoveredField;
    /**  */
    private JTextField versionOriginatedField;

    /**  */
    private JTextArea descriptionField;
    /**  */
    private JTextArea rationaleField;
    /**  */
    private JTextArea impactField;
    /**  */
    private IccbPanel iccbPanel;

    /**  */
    private ItemActionList<Boolean> thirdPartyListeners;
    /**  */
    private ItemActionList<Boolean> requirementsImpactListeners;

    /***************************************************************************
     * 
     **************************************************************************/
    public CrDefinitionPanel()
    {
        super( new GridBagLayout() );

        ActionListener thirdParyListener = new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                thirdPartyListeners.fireListeners( this,
                    thirdPartyField.isSelected() );
            }
        };

        ActionListener requirementImpactListener = new ActionListener()
        {
            @Override
            public void actionPerformed( ActionEvent e )
            {
                requirementsImpactListeners.fireListeners( this,
                    requirementsImpactField.isSelected() );
            }
        };

        thirdPartyListeners = new ItemActionList<Boolean>();
        requirementsImpactListeners = new ItemActionList<Boolean>();

        originatorField = new JTextField( "Kristine Kochanski", 25 );
        addFields( this, "Originator :", originatorField, 0, 0 );

        dateCreatedField = new JTextField( "8/22/10", 5 );
        dateCreatedField.setEditable( false );
        addFields( this, "Date Created :", dateCreatedField, 1, 0 );

        areaField = new JTextField( "Software", 5 );
        addFields( this, "Area :", areaField, 2, 0 );

        crTypeField = new JTextField( "Enhancement", 5 );
        addFields( this, "CR Type :", crTypeField, 3, 0 );

        priorityField = new JTextField( "Critical", 5 );
        addFields( this, "Priority :", priorityField, 4, 0 );

        designField = new JCheckBox( "" );
        addFields( this, "Design Required :", designField, 5, 0 );

        requirementsImpactField = new JCheckBox( "" );
        requirementsImpactField.addActionListener( requirementImpactListener );
        addFields( this, "Requirements Impact :", requirementsImpactField, 6, 0 );

        thirdPartyField = new JCheckBox( "" );
        thirdPartyField.addActionListener( thirdParyListener );
        addFields( this, "3rd Party IP :", thirdPartyField, 7, 0 );

        hwciField = new JTextField( "N/A", 5 );
        addFields( this, "HWCI :", hwciField, 8, 0 );

        estHoursField = new JTextField( "7", 5 );
        addFields( this, "Estimated Hours :", estHoursField, 9, 0 );

        descriptionField = new JTextArea();
        descriptionField.setText( "Doesn't work right" );
        addArea( this, "Description", descriptionField, 10 );

        rationaleField = new JTextArea();
        rationaleField.setText( "Because it doesn't work right" );
        addArea( this, "Rationale", rationaleField, 11 );

        impactField = new JTextArea();
        impactField.setText( "The bit that's supposed to work" );
        addArea( this, "Impact", impactField, 12 );

        // ---------------------------------------------------------------------
        // Column 2
        // ---------------------------------------------------------------------

        phaseDiscoveredField = new JTextField( "Implementation", 25 );
        addFields( this, "Phase Discovered :", phaseDiscoveredField, 0, 2 );

        phaseOriginatedField = new JTextField( "Requirements", 5 );
        addFields( this, "Phase Originated :", phaseOriginatedField, 1, 2 );

        versionDiscoveredField = new JTextField( "1.1.3", 5 );
        addFields( this, "Version Discovered :", versionDiscoveredField, 2, 2 );

        versionOriginatedField = new JTextField( "1.1.0", 5 );
        addFields( this, "Version Originated :", versionOriginatedField, 3, 2 );

        add( createListPanel( "Section :" ), new GridBagConstraints( 2, 4, 2,
            6, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
            new Insets( 4, 2, 2, 4 ), 0, 0 ) );

        // ---------------------------------------------------------------------
        // Filler
        // ---------------------------------------------------------------------

        iccbPanel = new IccbPanel();

        add( iccbPanel, new GridBagConstraints( 0, 13, 4, 1, 0.0, 1.0,
            GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets( 2, 4,
                4, 4 ), 0, 0 ) );

        add( Box.createVerticalStrut( 0 ), new GridBagConstraints( 0, 50, 6, 6,
            1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 ) );
    }

    /***************************************************************************
     * @param cr
     **************************************************************************/
    public void setData( IChangeRequest cr )
    {
        descriptionField.setText( cr.getDescription() );
        impactField.setText( cr.getImpact() );
        rationaleField.setText( cr.getRationale() );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addThirdPartyIpListener( ItemActionListener<Boolean> l )
    {
        thirdPartyListeners.addListener( l );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addRequirementImpactListener( ItemActionListener<Boolean> l )
    {
        requirementsImpactListeners.addListener( l );
    }

    /***************************************************************************
     * @param panel
     * @param title
     * @param field
     * @param row
     **************************************************************************/
    private static void addArea( JPanel panel, String title, JComponent field,
        int row )
    {
        JPanel titlePanel = new JPanel( new GridBagLayout() );
        JScrollPane scrollPane = new JScrollPane( field );

        titlePanel.setBorder( new TitledBorder( title ) );

        scrollPane.setPreferredSize( new Dimension( 150, 300 ) );

        titlePanel.add( scrollPane, new GridBagConstraints( 0, 0, 1, 1, 1.0,
            1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(
                0, 6, 6, 6 ), 0, 0 ) );

        panel.add( titlePanel, new GridBagConstraints( 0, row, 4, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets( 4, 4,
                4, 4 ), 0, 0 ) );
    }

    /***************************************************************************
     * @param panel
     * @param text
     * @param field
     * @param row
     * @param col
     **************************************************************************/
    private static void addFields( JPanel panel, String text, JComponent field,
        int row, int col )
    {
        JLabel label = new JLabel( text );

        panel.add( label, new GridBagConstraints( col, row, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets( 0, 4,
                0, 0 ), 0, 0 ) );
        panel.add( field, new GridBagConstraints( col + 1, row, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(
                4, 2, 2, 4 ), 0, 0 ) );
    }

    /***************************************************************************
     * @param title
     * @return
     **************************************************************************/
    private JPanel createListPanel( String title )
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        JLabel label = new JLabel( title );
        JButton openButton = new JButton();
        JList<String> list = new JList<String>();
        JScrollPane scrollPane = new JScrollPane( list );

        list.setPreferredSize( new Dimension( 90, 90 ) );
        scrollPane.setPreferredSize( new Dimension( 150, 100 ) );

        openButton.setIcon( IconConstants.loader.getIcon( IconConstants.OPEN_FOLDER_16 ) );

        panel.add( label, new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets( 0, 0,
                0, 4 ), 0, 0 ) );
        panel.add( openButton, new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets( 0, 4,
                4, 0 ), 0, 0 ) );

        panel.add( scrollPane, new GridBagConstraints( 0, 1, 2, 1, 1.0, 1.0,
            GridBagConstraints.SOUTHWEST, GridBagConstraints.BOTH, new Insets(
                2, 0, 0, 0 ), 0, 0 ) );

        return panel;
    }
}
