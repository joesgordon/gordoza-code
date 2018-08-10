package org.mc.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.model.IView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ScheduleMsgPanel implements IView<JComponent>
{
    /**  */
    private final JPanel view;
    /**  */
    private final MessageTextView msgField;
    /**  */
    private final JCheckBox scheduleCheckBox;
    /**  */
    private final JTextField msgCountTextField;
    /**  */
    private final JTextField msgDelayTextField;

    /***************************************************************************
     * 
     **************************************************************************/
    public ScheduleMsgPanel()
    {
        this.view = new JPanel( new GridBagLayout() );
        this.msgField = new MessageTextView();
        this.scheduleCheckBox = new JCheckBox( "Schedule Messages" );
        this.msgCountTextField = new JTextField( 5 );
        this.msgDelayTextField = new JTextField( 5 );

        msgCountTextField.setEnabled( false );
        msgDelayTextField.setEnabled( false );

        view.setBorder( BorderFactory.createTitledBorder( "" ) );

        scheduleCheckBox.addActionListener( new ScheduleCheckListener() );

        view.add( msgField.getView(),
            new GridBagConstraints( 0, 0, 5, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 6, 6, 6, 6 ), 0, 0 ) );

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

        msgField.setEditable( false );
        msgCountTextField.setEnabled( false );
        msgDelayTextField.setEnabled( false );
        scheduleCheckBox.setEnabled( false );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addSendListener( ItemActionListener<String> l )
    {
        msgField.addEnterListener( l );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public byte[] getMessageText()
    {
        return msgField.getData();
    }

    /***************************************************************************
     * @param text
     **************************************************************************/
    public void setMessageText( byte[] text )
    {
        msgField.setData( text );
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
        msgField.setEditable( bound );
        scheduleCheckBox.setEnabled( bound );

        scheduleCheckBox.setSelected( false );

        msgCountTextField.setEnabled( scheduleCheckBox.isSelected() );
        msgDelayTextField.setEnabled( scheduleCheckBox.isSelected() );
    }

    /***************************************************************************
     * @param editable
     **************************************************************************/
    public void setEditable( boolean editable )
    {
        msgField.setEditable( editable );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void selectAll()
    {
        msgField.selectAll();
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
