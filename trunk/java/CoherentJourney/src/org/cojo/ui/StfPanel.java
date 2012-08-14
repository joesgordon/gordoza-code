package org.cojo.ui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import org.cojo.model.ISoftwareTask;

/*******************************************************************************
 * 
 ******************************************************************************/
public class StfPanel extends JPanel
{
    private JTabbedPane tabbedPane;

    private JTextField numberField;
    private JTextField titleField;
    private JComboBox leadField;
    private JCheckBox codeReviewField;
    private JTextField estimatedHoursField;
    private JTextField actualHoursField;
    private JTextArea descriptionField;
    private JTextArea unitTestDescField;
    private JTextArea unitTestResultsField;

    private FindingsPanel codeReviewPanel;

    /***************************************************************************
     * 
     **************************************************************************/
    public StfPanel()
    {
        super( new BorderLayout() );

        JPanel stfPanel = createStfPanel();
        JScrollPane stfScrollPane = new JScrollPane( stfPanel );

        stfScrollPane.setBorder( BorderFactory.createEmptyBorder() );

        tabbedPane = new JTabbedPane();
        codeReviewPanel = new FindingsPanel();

        tabbedPane.addTab( "STF", stfScrollPane );
        tabbedPane.addTab( "Code Review", codeReviewPanel );

        numberField.setEditable( false );

        add( tabbedPane, BorderLayout.CENTER );
    }

    /***************************************************************************
     * @param task
     **************************************************************************/
    public void setData( ISoftwareTask task )
    {
        numberField.setText( "" + task.getNumber() );
        titleField.setText( task.getTitle() );
        leadField.setSelectedItem( task.getLead() );
        codeReviewField.setSelected( task.isCodeReviewRequired() );
        estimatedHoursField.setText( "" + task.getEstimatedHours() );
        actualHoursField.setText( "" + task.getActualHours() );
        descriptionField.setText( task.getDescription() );
        unitTestDescField.setText( task.getUnitTestDescription() );
        unitTestResultsField.setText( task.getUnitTestResults() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createStfPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );

        numberField = new JTextField( 25 );
        titleField = new JTextField();
        leadField = new JComboBox();
        codeReviewField = new JCheckBox();
        estimatedHoursField = new JTextField();
        actualHoursField = new JTextField();
        descriptionField = new JTextArea();
        unitTestDescField = new JTextArea();
        unitTestResultsField = new JTextArea();

        addFields( panel, "Task # :", numberField, 0, 0 );
        addFields( panel, "Title :", titleField, 1, 0 );
        addFields( panel, "Lead :", leadField, 2, 0 );
        addFields( panel, "Code Review Required :", codeReviewField, 3, 0 );
        addFields( panel, "Estimated Hours :", estimatedHoursField, 4, 0 );
        addFields( panel, "Actual Hours :", actualHoursField, 5, 0 );

        addArea( panel, "Description", descriptionField, 6 );
        addArea( panel, "Unit Test Description", unitTestDescField, 7 );
        addArea( panel, "Unit Test Results", unitTestResultsField, 8 );

        panel.add( Box.createVerticalStrut( 0 ), new GridBagConstraints( 0, 50,
            2, 6, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH,
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
