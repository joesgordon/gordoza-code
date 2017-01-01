package org.cojo.ui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import org.cojo.model.IChangeRequest;
import org.jutils.IconConstants;
import org.jutils.ui.event.ItemActionList;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class CrDefinitionPanel implements IView<JPanel>
{
    /**  */
    private final JPanel view;
    /**  */
    private final JTextField originatorField;
    /**  */
    private final JTextField dateCreatedField;
    /**  */
    private final JTextField priorityField;
    /**  */
    private final JTextField areaField;
    /**  */
    private final JCheckBox thirdPartyField;
    /**  */
    private final JCheckBox requirementsImpactField;
    /**  */
    private final JCheckBox designField;
    /**  */
    private final JTextField hwciField;
    /**  */
    private final JTextField crTypeField;
    /**  */
    private final JTextField phaseDiscoveredField;
    /**  */
    private final JTextField phaseOriginatedField;
    /**  */
    private final JTextField estHoursField;

    /**  */
    private final JTextField versionDiscoveredField;
    /**  */
    private final JTextField versionOriginatedField;

    /**  */
    private final JTextArea descriptionField;
    /**  */
    private final JTextArea rationaleField;
    /**  */
    private final JTextArea impactField;
    /**  */
    private final IccbPanel iccbPanel;

    /**  */
    private final ItemActionList<Boolean> thirdPartyListeners;
    /**  */
    private final ItemActionList<Boolean> requirementsImpactListeners;

    /***************************************************************************
     * 
     **************************************************************************/
    public CrDefinitionPanel()
    {
        this.view = new JPanel( new GridBagLayout() );

        thirdPartyListeners = new ItemActionList<Boolean>();
        requirementsImpactListeners = new ItemActionList<Boolean>();

        originatorField = new JTextField( "Kristine Kochanski", 25 );
        addFields( view, "Originator :", originatorField, 0, 0 );

        dateCreatedField = new JTextField( "8/22/10", 5 );
        dateCreatedField.setEditable( false );
        addFields( view, "Date Created :", dateCreatedField, 1, 0 );

        areaField = new JTextField( "Software", 5 );
        addFields( view, "Area :", areaField, 2, 0 );

        crTypeField = new JTextField( "Enhancement", 5 );
        addFields( view, "CR Type :", crTypeField, 3, 0 );

        priorityField = new JTextField( "Critical", 5 );
        addFields( view, "Priority :", priorityField, 4, 0 );

        designField = new JCheckBox( "" );
        addFields( view, "Design Required :", designField, 5, 0 );

        requirementsImpactField = new JCheckBox( "" );
        requirementsImpactField.addActionListener(
            ( e ) -> requirementsImpactListeners.fireListeners( this,
                requirementsImpactField.isSelected() ) );
        addFields( view, "Requirements Impact :", requirementsImpactField, 6,
            0 );

        thirdPartyField = new JCheckBox( "" );
        thirdPartyField.addActionListener(
            ( e ) -> thirdPartyListeners.fireListeners( this,
                thirdPartyField.isSelected() ) );
        addFields( view, "3rd Party IP :", thirdPartyField, 7, 0 );

        hwciField = new JTextField( "N/A", 5 );
        addFields( view, "HWCI :", hwciField, 8, 0 );

        estHoursField = new JTextField( "7", 5 );
        addFields( view, "Estimated Hours :", estHoursField, 9, 0 );

        descriptionField = new JTextArea();
        descriptionField.setText( "Doesn't work right" );
        addArea( view, "Description", descriptionField, 10 );

        rationaleField = new JTextArea();
        rationaleField.setText( "Because it doesn't work right" );
        addArea( view, "Rationale", rationaleField, 11 );

        impactField = new JTextArea();
        impactField.setText( "The bit that's supposed to work" );
        addArea( view, "Impact", impactField, 12 );

        // ---------------------------------------------------------------------
        // Column 2
        // ---------------------------------------------------------------------

        phaseDiscoveredField = new JTextField( "Implementation", 25 );
        addFields( view, "Phase Discovered :", phaseDiscoveredField, 0, 2 );

        phaseOriginatedField = new JTextField( "Requirements", 5 );
        addFields( view, "Phase Originated :", phaseOriginatedField, 1, 2 );

        versionDiscoveredField = new JTextField( "1.1.3", 5 );
        addFields( view, "Version Discovered :", versionDiscoveredField, 2, 2 );

        versionOriginatedField = new JTextField( "1.1.0", 5 );
        addFields( view, "Version Originated :", versionOriginatedField, 3, 2 );

        view.add( createListPanel( "Section :" ),
            new GridBagConstraints( 2, 4, 2, 6, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                new Insets( 4, 2, 2, 4 ), 0, 0 ) );

        // ---------------------------------------------------------------------
        // Filler
        // ---------------------------------------------------------------------

        iccbPanel = new IccbPanel();

        view.add( iccbPanel.getView(),
            new GridBagConstraints( 0, 13, 4, 1, 0.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                new Insets( 2, 4, 4, 4 ), 0, 0 ) );

        view.add( Box.createVerticalStrut( 0 ),
            new GridBagConstraints( 0, 50, 6, 6, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
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

        titlePanel.add( scrollPane,
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                new Insets( 0, 6, 6, 6 ), 0, 0 ) );

        panel.add( titlePanel,
            new GridBagConstraints( 0, row, 4, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                new Insets( 4, 4, 4, 4 ), 0, 0 ) );
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

        panel.add( label,
            new GridBagConstraints( col, row, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets( 0, 4, 0, 0 ), 0, 0 ) );
        panel.add( field,
            new GridBagConstraints( col + 1, row, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets( 4, 2, 2, 4 ), 0, 0 ) );
    }

    /***************************************************************************
     * @param title
     * @return
     **************************************************************************/
    private static JPanel createListPanel( String title )
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        JLabel label = new JLabel( title );
        JButton openButton = new JButton();
        JList<String> list = new JList<String>();
        JScrollPane scrollPane = new JScrollPane( list );

        list.setPreferredSize( new Dimension( 90, 90 ) );
        scrollPane.setPreferredSize( new Dimension( 150, 100 ) );

        openButton.setIcon(
            IconConstants.getIcon( IconConstants.OPEN_FOLDER_16 ) );

        panel.add( label,
            new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets( 0, 0, 0, 4 ), 0, 0 ) );
        panel.add( openButton,
            new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets( 0, 4, 4, 0 ), 0, 0 ) );

        panel.add( scrollPane,
            new GridBagConstraints( 0, 1, 2, 1, 1.0, 1.0,
                GridBagConstraints.SOUTHWEST, GridBagConstraints.BOTH,
                new Insets( 2, 0, 0, 0 ), 0, 0 ) );

        return panel;
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
