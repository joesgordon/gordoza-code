package org.mc.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class McInputPanel implements IView<JPanel>
{
    /**  */
    private final JPanel view;
    /**  */
    private final JEditorPane sendTextArea;
    /**  */
    private final JButton sendButton;
    /**  */
    private final JCheckBox scheduleCheckBox;
    /**  */
    private final JTextField msgCountTextField;
    /**  */
    private final JTextField msgDelayTextField;

    /***************************************************************************
     * 
     **************************************************************************/
    public McInputPanel()
    {
        this.view = new JPanel( new GridBagLayout() );
        this.sendTextArea = new JEditorPane();
        JScrollPane sendScrollPane = new JScrollPane( sendTextArea );
        this.scheduleCheckBox = new JCheckBox( "Schedule Messages" );
        this.msgCountTextField = new JTextField( 5 );
        this.msgDelayTextField = new JTextField( 5 );
        this.sendButton = new JButton();

        msgCountTextField.setEnabled( false );
        msgDelayTextField.setEnabled( false );
        sendButton.setIcon(
            IconConstants.getIcon( IconConstants.NAV_NEXT_24 ) );
        sendButton.setText( "Send" );

        view.setBorder( BorderFactory.createTitledBorder( "" ) );

        scheduleCheckBox.addActionListener( new ScheduleCheckListener() );

        view.add( sendScrollPane,
            new GridBagConstraints( 0, 0, 5, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 6, 6, 6, 6 ), 0, 0 ) );
        view.add( sendButton,
            new GridBagConstraints( 5, 0, 1, 2, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets( 6, 0, 6, 6 ), 0, 10 ) );

        view.add( scheduleCheckBox,
            new GridBagConstraints( 0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets( 0, 6, 6, 6 ), 0, 10 ) );
        view.add( new JLabel( "#" ),
            new GridBagConstraints( 1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets( 0, 0, 6, 6 ), 0, 10 ) );
        view.add( msgCountTextField,
            new GridBagConstraints( 2, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets( 0, 0, 6, 6 ), 0, 10 ) );
        view.add( new JLabel( "Delay" ),
            new GridBagConstraints( 3, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets( 0, 0, 6, 6 ), 0, 10 ) );
        view.add( msgDelayTextField,
            new GridBagConstraints( 4, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets( 0, 0, 6, 6 ), 0, 10 ) );

        msgCountTextField.setMinimumSize(
            msgDelayTextField.getPreferredSize() );
        msgDelayTextField.setMinimumSize(
            msgDelayTextField.getPreferredSize() );

        sendScrollPane.setMinimumSize( new Dimension( 200, 100 ) );
        sendScrollPane.setPreferredSize( view.getMinimumSize() );

        sendTextArea.setEnabled( false );
        msgCountTextField.setEnabled( false );
        msgDelayTextField.setEnabled( false );
        scheduleCheckBox.setEnabled( false );
        sendButton.setEnabled( false );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addSendActionListener( ActionListener l )
    {
        sendButton.addActionListener( l );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String getMessageText()
    {
        return sendTextArea.getText();
    }

    /***************************************************************************
     * @param text
     **************************************************************************/
    public void setMessageText( String text )
    {
        sendTextArea.setText( text );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isScheduling()
    {
        return scheduleCheckBox.isSelected();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getMessageCount()
    {
        return Integer.parseInt( msgCountTextField.getText() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getSendDelay()
    {
        return Integer.parseInt( msgDelayTextField.getText() );
    }

    /***************************************************************************
     * @param bound
     **************************************************************************/
    public void setBound( boolean bound )
    {
        sendButton.setEnabled( bound );
        sendTextArea.setEnabled( bound );
        scheduleCheckBox.setEnabled( bound );

        scheduleCheckBox.setSelected( false );

        msgCountTextField.setEnabled( scheduleCheckBox.isSelected() );
        msgDelayTextField.setEnabled( scheduleCheckBox.isSelected() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void selectAll()
    {
        sendTextArea.selectAll();
    }

    /***************************************************************************
     * 
     **************************************************************************/
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
