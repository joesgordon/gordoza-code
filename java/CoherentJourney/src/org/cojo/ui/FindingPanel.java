package org.cojo.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.cojo.data.Finding;
import org.cojo.data.Project;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class FindingPanel implements IView<JPanel>
{
    /**  */
    private final JPanel view;
    /**  */
    private final JTextField numberField;
    /**  */
    private final JTextField userField;
    /**  */
    private final JTextField dateField;
    /**  */
    private final JCheckBox acceptedField;
    /**  */
    private final JTextArea descriptionField;
    /**  */
    private final JTextArea commentsField;

    /**  */
    private Project project;

    /***************************************************************************
     * 
     **************************************************************************/
    public FindingPanel()
    {
        this.view = new JPanel( new BorderLayout() );
        this.numberField = new JTextField();
        this.userField = new JTextField();
        this.dateField = new JTextField();
        this.acceptedField = new JCheckBox();
        this.descriptionField = new JTextArea();
        this.commentsField = new JTextArea();

        JScrollPane scrollPane = new JScrollPane( createMainPanel() );

        view.add( scrollPane, BorderLayout.CENTER );
    }

    /***************************************************************************
     * @param finding
     **************************************************************************/
    public void setData( Finding finding )
    {
        numberField.setText( "" + finding.id );
        userField.setText( project.getUser( finding.userId ).getName() );
        dateField.setText( finding.time.toString() );
        acceptedField.setSelected( finding.accepted );
        descriptionField.setText( finding.description );
        commentsField.setText( finding.comments );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createMainPanel()
    {
        JPanel mainPanel = new JPanel( new GridBagLayout() );

        numberField.setEditable( false );
        userField.setEditable( false );
        dateField.setEditable( false );

        addFields( mainPanel, "Finding # :", numberField, 0, 0 );
        addFields( mainPanel, "User :", userField, 1, 0 );
        addFields( mainPanel, "Date :", dateField, 2, 0 );
        addFields( mainPanel, "Accepted :", acceptedField, 3, 0 );

        addArea( mainPanel, "Description", descriptionField, 4 );
        addArea( mainPanel, "Comments", commentsField, 5 );

        mainPanel.add( Box.createVerticalStrut( 0 ),
            new GridBagConstraints( 0, 50, 2, 6, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );

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

        titlePanel.add( scrollPane,
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                new Insets( 0, 6, 6, 6 ), 0, 0 ) );

        panel.add( titlePanel,
            new GridBagConstraints( 0, row, 2, 1, 0.0, 0.0,
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
     * 
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }

    public void setProject( Project project )
    {
        this.project = project;
    }
}
