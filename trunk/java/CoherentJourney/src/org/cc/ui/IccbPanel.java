package org.cc.ui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import org.jutils.IconConstants;

public class IccbPanel extends JPanel
{
    private JComboBox leadField;
    private JComboBox seField;
    private JComboBox proposedVersionField;
    private JTextField dateApprovedField;
    private JButton dateApprovedButton;
    private JTextArea commentField;
    private JComboBox releasedVersionField;
    private JComboBox eventField;

    public IccbPanel()
    {
        super( new GridBagLayout() );

        JLabel leadLabel = new JLabel( "CR Lead :" );
        leadField = new JComboBox();

        JLabel seLabel = new JLabel( "Systems Engineer :" );
        seField = new JComboBox();

        JLabel proposedVersionLabel = new JLabel( "Proposed Version :" );
        proposedVersionField = new JComboBox();

        JLabel dateApprovedLabel = new JLabel( "Date Approved :" );
        dateApprovedField = new JTextField( "Date", 25 );
        dateApprovedButton = new JButton( IconConstants
            .getIcon( IconConstants.CALENDAR_16 ) );

        JLabel releasedVersionLabel = new JLabel( "Released Version :" );
        releasedVersionField = new JComboBox();

        JLabel eventLabel = new JLabel( "Event Supported :" );
        eventField = new JComboBox();

        JLabel commentLabel = new JLabel( "Comments :" );
        commentField = new JTextArea();
        commentField.setPreferredSize( new Dimension( 200, 200 ) );

        seField.setPreferredSize( dateApprovedField.getPreferredSize() );

        JScrollPane commentScollPane = new JScrollPane( commentField );

        // ---------------------------------------------------------------------
        // Row 0
        // ---------------------------------------------------------------------

        add( leadLabel, new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets( 4, 4,
                2, 2 ), 0, 0 ) );
        add( leadField, new GridBagConstraints( 1, 0, 2, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(
                4, 2, 2, 2 ), 0, 0 ) );

        add( seLabel, new GridBagConstraints( 3, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets( 2, 2,
                2, 2 ), 0, 0 ) );
        add( seField, new GridBagConstraints( 4, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(
                2, 2, 2, 4 ), 0, 0 ) );

        // ---------------------------------------------------------------------
        // Row 1
        // ---------------------------------------------------------------------

        add( proposedVersionLabel, new GridBagConstraints( 0, 1, 1, 1, 0.0,
            0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
                2, 4, 2, 2 ), 0, 0 ) );
        add( proposedVersionField, new GridBagConstraints( 1, 1, 2, 1, 0.0,
            0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets( 2, 2, 2, 2 ), 0, 0 ) );

        add( releasedVersionLabel, new GridBagConstraints( 3, 1, 1, 1, 0.0,
            0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(
                2, 2, 2, 2 ), 0, 0 ) );
        add( releasedVersionField, new GridBagConstraints( 4, 1, 1, 1, 0.0,
            0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
            new Insets( 2, 2, 2, 4 ), 0, 0 ) );

        // ---------------------------------------------------------------------
        // Row 2
        // ---------------------------------------------------------------------

        add( dateApprovedLabel, new GridBagConstraints( 0, 2, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets( 2, 4,
                2, 2 ), 0, 0 ) );
        add( dateApprovedField, new GridBagConstraints( 1, 2, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(
                2, 2, 2, 2 ), 0, 0 ) );
        add( dateApprovedButton, new GridBagConstraints( 2, 2, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets( 2, 2,
                2, 2 ), 0, 0 ) );

        add( eventLabel, new GridBagConstraints( 3, 2, 1, 1, 0.0, 0.0,
            GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets( 2, 2,
                2, 2 ), 0, 0 ) );
        add( eventField, new GridBagConstraints( 4, 2, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(
                2, 2, 2, 4 ), 0, 0 ) );

        // ---------------------------------------------------------------------
        // Row 3
        // ---------------------------------------------------------------------

        add( commentLabel, new GridBagConstraints( 0, 3, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 10,
                4, 2, 4 ), 0, 0 ) );

        // ---------------------------------------------------------------------
        // Row 4
        // ---------------------------------------------------------------------

        add( commentScollPane, new GridBagConstraints( 0, 4, 6, 1, 1.0, 1.0,
            GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets( 2, 4,
                4, 4 ), 0, 0 ) );

        setBorder( new TitledBorder( "ICCB" ) );
    }
}
