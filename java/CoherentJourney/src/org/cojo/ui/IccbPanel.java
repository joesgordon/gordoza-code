package org.cojo.ui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import org.jutils.IconConstants;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class IccbPanel implements IView<JPanel>
{
    /**  */
    private final JPanel view;
    /**  */
    private final JComboBox<String> leadField;
    /**  */
    private final JComboBox<String> seField;
    /**  */
    private final JComboBox<String> proposedVersionField;
    /**  */
    private final JTextField dateApprovedField;
    /**  */
    private final JButton dateApprovedButton;
    /**  */
    private final JTextArea commentField;
    /**  */
    private final JComboBox<String> releasedVersionField;
    /**  */
    private final JComboBox<String> eventField;

    /***************************************************************************
     * 
     **************************************************************************/
    public IccbPanel()
    {
        this.view = new JPanel( new GridBagLayout() );

        JLabel leadLabel = new JLabel( "CR Lead :" );
        this.leadField = new JComboBox<String>();

        JLabel seLabel = new JLabel( "Systems Engineer :" );
        this.seField = new JComboBox<String>();

        JLabel proposedVersionLabel = new JLabel( "Proposed Version :" );
        this.proposedVersionField = new JComboBox<String>();

        JLabel dateApprovedLabel = new JLabel( "Date Approved :" );
        this.dateApprovedField = new JTextField( "Date", 25 );
        this.dateApprovedButton = new JButton(
            IconConstants.getIcon( IconConstants.CALENDAR_16 ) );

        JLabel releasedVersionLabel = new JLabel( "Released Version :" );
        this.releasedVersionField = new JComboBox<String>();

        JLabel eventLabel = new JLabel( "Event Supported :" );
        this.eventField = new JComboBox<String>();

        JLabel commentLabel = new JLabel( "Comments :" );
        this.commentField = new JTextArea();
        commentField.setPreferredSize( new Dimension( 200, 200 ) );

        seField.setPreferredSize( dateApprovedField.getPreferredSize() );

        JScrollPane commentScollPane = new JScrollPane( commentField );

        // ---------------------------------------------------------------------
        // Row 0
        // ---------------------------------------------------------------------

        view.add( leadLabel,
            new GridBagConstraints( 0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets( 4, 4, 2, 2 ), 0, 0 ) );
        view.add( leadField,
            new GridBagConstraints( 1, 0, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets( 4, 2, 2, 2 ), 0, 0 ) );

        view.add( seLabel,
            new GridBagConstraints( 3, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );
        view.add( seField,
            new GridBagConstraints( 4, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets( 2, 2, 2, 4 ), 0, 0 ) );

        // ---------------------------------------------------------------------
        // Row 1
        // ---------------------------------------------------------------------

        view.add( proposedVersionLabel,
            new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets( 2, 4, 2, 2 ), 0, 0 ) );
        view.add( proposedVersionField,
            new GridBagConstraints( 1, 1, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );

        view.add( releasedVersionLabel,
            new GridBagConstraints( 3, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );
        view.add( releasedVersionField,
            new GridBagConstraints( 4, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets( 2, 2, 2, 4 ), 0, 0 ) );

        // ---------------------------------------------------------------------
        // Row 2
        // ---------------------------------------------------------------------

        view.add( dateApprovedLabel,
            new GridBagConstraints( 0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets( 2, 4, 2, 2 ), 0, 0 ) );
        view.add( dateApprovedField,
            new GridBagConstraints( 1, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );
        view.add( dateApprovedButton,
            new GridBagConstraints( 2, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );

        view.add( eventLabel,
            new GridBagConstraints( 3, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE,
                new Insets( 2, 2, 2, 2 ), 0, 0 ) );
        view.add( eventField,
            new GridBagConstraints( 4, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                new Insets( 2, 2, 2, 4 ), 0, 0 ) );

        // ---------------------------------------------------------------------
        // Row 3
        // ---------------------------------------------------------------------

        view.add( commentLabel,
            new GridBagConstraints( 0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 10, 4, 2, 4 ), 0, 0 ) );

        // ---------------------------------------------------------------------
        // Row 4
        // ---------------------------------------------------------------------

        view.add( commentScollPane,
            new GridBagConstraints( 0, 4, 6, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                new Insets( 2, 4, 4, 4 ), 0, 0 ) );

        view.setBorder( new TitledBorder( "ICCB" ) );
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
