package org.mc.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.jutils.IconConstants;

/*******************************************************************************
 * 
 ******************************************************************************/
public class McInputPanel extends JPanel
{
    /**  */
    private JEditorPane sendTextArea;

    /**  */
    private JButton sendButton;

    /**  */
    private JCheckBox scheduleCheckBox;

    /**  */
    private JTextField msgCountTextField;

    /**  */
    private JTextField msgDelayTextField;

    /***************************************************************************
     * 
     **************************************************************************/
    public McInputPanel()
    {
        sendTextArea = new JEditorPane();
        JScrollPane sendScrollPane = new JScrollPane( sendTextArea );
        scheduleCheckBox = new JCheckBox( "Schedule Messages" );
        msgCountTextField = new JTextField( 5 );
        msgDelayTextField = new JTextField( 5 );
        sendButton = new JButton();

        msgCountTextField.setEnabled( false );
        msgDelayTextField.setEnabled( false );
        sendButton.setIcon( IconConstants.getIcon( IconConstants.FORWARD_24 ) );
        sendButton.setText( "Send" );

        setLayout( new GridBagLayout() );
        setBorder( BorderFactory.createTitledBorder( "" ) );

        scheduleCheckBox.addActionListener( new ScheduleCheckListener() );

        add( sendScrollPane, new GridBagConstraints( 0, 0, 5, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets( 6,
                6, 6, 6 ), 0, 0 ) );
        add( sendButton, new GridBagConstraints( 5, 0, 1, 2, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 6, 0, 6, 6 ), 0, 10 ) );

        add( scheduleCheckBox, new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 6, 6, 6 ), 0, 10 ) );
        add( new JLabel( "#" ), new GridBagConstraints( 1, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets( 0,
                0, 6, 6 ), 0, 10 ) );
        add( msgCountTextField, new GridBagConstraints( 2, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets( 0,
                0, 6, 6 ), 0, 10 ) );
        add( new JLabel( "Delay" ), new GridBagConstraints( 3, 1, 1, 1, 0.0,
            0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 0, 0, 6, 6 ), 0, 10 ) );
        add( msgDelayTextField, new GridBagConstraints( 4, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets( 0, 0,
                6, 6 ), 0, 10 ) );

        msgCountTextField.setMinimumSize( msgDelayTextField.getPreferredSize() );
        msgDelayTextField.setMinimumSize( msgDelayTextField.getPreferredSize() );

        sendScrollPane.setMinimumSize( new Dimension( 200, 100 ) );
        sendScrollPane.setPreferredSize( getMinimumSize() );

        sendTextArea.setEnabled( false );
        msgCountTextField.setEnabled( false );
        msgDelayTextField.setEnabled( false );
        scheduleCheckBox.setEnabled( false );
        sendButton.setEnabled( false );
    }

    public void addSendActionListener( ActionListener l )
    {
        sendButton.addActionListener( l );
    }

    public String getMessageText()
    {
        return sendTextArea.getText();
    }

    public void setMessageText( String text )
    {
        sendTextArea.setText( text );
    }

    public boolean isScheduling()
    {
        return scheduleCheckBox.isSelected();
    }

    public int getMessageCount()
    {
        return Integer.parseInt( msgCountTextField.getText() );
    }

    public int getSendDelay()
    {
        return Integer.parseInt( msgDelayTextField.getText() );
    }

    public void setBound( boolean bound )
    {
        sendButton.setEnabled( bound );
        sendTextArea.setEnabled( bound );
        scheduleCheckBox.setEnabled( bound );

        scheduleCheckBox.setSelected( false );

        msgCountTextField.setEnabled( scheduleCheckBox.isSelected() );
        msgDelayTextField.setEnabled( scheduleCheckBox.isSelected() );
    }

    private class ScheduleCheckListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            JCheckBox cb = ( JCheckBox )e.getSource();

            msgCountTextField.setEnabled( cb.isSelected() );
            msgDelayTextField.setEnabled( cb.isSelected() );
        }
    }
}
