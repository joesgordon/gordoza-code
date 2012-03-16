package org.cc.edit.ui.panels.model;

import java.awt.*;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JLabel;
import javax.swing.JTextField;

import org.cc.data.LockInfo;
import org.cc.edit.ui.InfoPanel;
import org.jutils.ui.calendar.DateTimePanel;
import org.jutils.ui.event.updater.DocumentUpdater;
import org.jutils.ui.event.updater.IDataUpdater;

/*******************************************************************************
 * 
 ******************************************************************************/
public class LockInfoPanel extends InfoPanel<LockInfo>
{
    /**  */
    private JTextField userField;
    /**  */
    private JTextField reasonField;
    /**  */
    private DateTimePanel timeField;

    /***************************************************************************
     * 
     **************************************************************************/
    public LockInfoPanel()
    {
        super( new GridBagLayout() );

        userField = new JTextField( 10 );
        reasonField = new JTextField( 10 );
        timeField = new DateTimePanel();

        userField.getDocument().addDocumentListener(
            new DocumentUpdater( new UserUpdater() ) );

        reasonField.getDocument().addDocumentListener(
            new DocumentUpdater( new ReasonUpdater() ) );

        timeField.addDataUpdater( new DateUpdater() );

        add( new JLabel( "User :" ), new GridBagConstraints( 0, 0, 1, 1, 0.0,
            0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                0, 0, 0, 0 ), 0, 0 ) );
        add( userField, new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(
                0, 4, 5, 4 ), 0, 0 ) );

        add( new JLabel( "Reason :" ), new GridBagConstraints( 0, 2, 1, 1, 0.0,
            0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                0, 0, 0, 0 ), 0, 0 ) );
        add( reasonField, new GridBagConstraints( 0, 3, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(
                0, 4, 5, 4 ), 0, 0 ) );

        add( new JLabel( "Time :" ), new GridBagConstraints( 0, 4, 1, 1, 0.0,
            0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                0, 0, 0, 0 ), 0, 0 ) );
        add( timeField, new GridBagConstraints( 0, 5, 1, 1, 1.0, 0.0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(
                0, 4, 4, 4 ), 0, 0 ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    protected void displayData( LockInfo data )
    {
        GregorianCalendar cal = new GregorianCalendar();

        cal.setTime( new Date( data.getTime() * 1000 ) );
        userField.setText( data.getUser() );
        reasonField.setText( data.getReason() );
        timeField.setDate( cal );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class DateUpdater implements IDataUpdater
    {
        @Override
        public void updateData()
        {
            getData().setTime( timeField.getDate().getTimeInMillis() / 1000 );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class UserUpdater implements IDataUpdater
    {
        @Override
        public void updateData()
        {
            getData().setUser( userField.getText() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class ReasonUpdater implements IDataUpdater
    {
        @Override
        public void updateData()
        {
            getData().setReason( reasonField.getText() );
        }
    }
}
