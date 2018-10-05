package org.cojo.ui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import org.cojo.data.*;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class StfPanel implements IView<JPanel>
{
    private final JPanel view;
    private final JTabbedPane tabbedPane;

    private final JTextField numberField;
    private final JTextField titleField;
    private final JComboBox<User> leadField;
    private final JCheckBox codeReviewField;
    private final JTextField estimatedHoursField;
    private final JTextField actualHoursField;
    private final JTextArea descriptionField;
    private final JTextArea unitTestDescField;
    private final JTextArea unitTestResultsField;

    private final FindingsPanel codeReviewPanel;
    private Project project;

    /***************************************************************************
     * 
     **************************************************************************/
    public StfPanel()
    {
        this.view = new JPanel( new BorderLayout() );
        this.numberField = new JTextField( 25 );
        this.titleField = new JTextField();
        this.leadField = new JComboBox<User>();
        this.codeReviewField = new JCheckBox();
        this.estimatedHoursField = new JTextField();
        this.actualHoursField = new JTextField();
        this.descriptionField = new JTextArea();
        this.unitTestDescField = new JTextArea();
        this.unitTestResultsField = new JTextArea();

        JPanel stfPanel = createStfPanel();
        JScrollPane stfScrollPane = new JScrollPane( stfPanel );

        stfScrollPane.setBorder( BorderFactory.createEmptyBorder() );

        tabbedPane = new JTabbedPane();
        codeReviewPanel = new FindingsPanel();

        tabbedPane.addTab( "STF", stfScrollPane );
        tabbedPane.addTab( "Code Review", codeReviewPanel.getView() );

        numberField.setEditable( false );

        view.add( tabbedPane, BorderLayout.CENTER );
    }

    /***************************************************************************
     * @param task
     **************************************************************************/
    public void setData( SoftwareTask task )
    {
        numberField.setText( "" + task.id );
        titleField.setText( task.title );
        leadField.setSelectedItem( project.getUser( task.leadUserId ) );
        codeReviewField.setSelected( task.codeReviewRequired );
        estimatedHoursField.setText( "" + task.estimatedHours );
        actualHoursField.setText( "" + task.actualHours );
        descriptionField.setText( task.description );
        unitTestDescField.setText( task.unitTestDescription );
        unitTestResultsField.setText( task.unitTestResults );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createStfPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );

        addFields( panel, "Task # :", numberField, 0, 0 );
        addFields( panel, "Title :", titleField, 1, 0 );
        addFields( panel, "Lead :", leadField, 2, 0 );
        addFields( panel, "Code Review Required :", codeReviewField, 3, 0 );
        addFields( panel, "Estimated Hours :", estimatedHoursField, 4, 0 );
        addFields( panel, "Actual Hours :", actualHoursField, 5, 0 );

        addArea( panel, "Description", descriptionField, 6 );
        addArea( panel, "Unit Test Description", unitTestDescField, 7 );
        addArea( panel, "Unit Test Results", unitTestResultsField, 8 );

        panel.add( Box.createVerticalStrut( 0 ),
            new GridBagConstraints( 0, 50, 2, 6, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );

        return panel;
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
