package org.cc.ui;

import java.awt.*;
import java.util.Date;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import org.cc.model.IFinding;

/**
 * 
 */
public class FindingPanel extends JPanel
{
    private JTextField numberField;
    private JTextField userField;
    private JTextField dateField;
    private JCheckBox acceptedField;
    private JTextArea descriptionField;
    private JTextArea commentsField;

    /**
     * 
     */
    public FindingPanel()
    {
        super( new BorderLayout() );

        JScrollPane scrollPane = new JScrollPane( createMainPanel() );

        add( scrollPane, BorderLayout.CENTER );
    }

    /**
     * @param finding
     */
    public void setData( IFinding finding )
    {
        Date d = new Date( finding.getDate() );

        numberField.setText( "" + finding.getNumber() );
        userField.setText( finding.getUser().getName() );
        dateField.setText( d.toString() );
        acceptedField.setSelected( finding.isAccepted() );
        descriptionField.setText( finding.getDescription() );
        commentsField.setText( finding.getComments() );
    }

    /**
     * @return
     */
    private JPanel createMainPanel()
    {
        JPanel mainPanel = new JPanel( new GridBagLayout() );

        numberField = new JTextField();
        userField = new JTextField();
        dateField = new JTextField();
        acceptedField = new JCheckBox();
        descriptionField = new JTextArea();
        commentsField = new JTextArea();

        numberField.setEditable( false );
        userField.setEditable( false );
        dateField.setEditable( false );

        addFields( mainPanel, "Finding # :", numberField, 0, 0 );
        addFields( mainPanel, "User :", userField, 1, 0 );
        addFields( mainPanel, "Date :", dateField, 2, 0 );
        addFields( mainPanel, "Accepted :", acceptedField, 3, 0 );

        addArea( mainPanel, "Description", descriptionField, 4 );
        addArea( mainPanel, "Comments", commentsField, 5 );

        mainPanel.add( Box.createVerticalStrut( 0 ), new GridBagConstraints( 0,
            50, 2, 6, 1.0, 1.0, GridBagConstraints.WEST,
            GridBagConstraints.BOTH, new Insets( 0, 0, 0, 0 ), 0, 0 ) );

        return mainPanel;
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

        panel.add( titlePanel, new GridBagConstraints( 0, row, 2, 1, 0.0, 0.0,
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
}
