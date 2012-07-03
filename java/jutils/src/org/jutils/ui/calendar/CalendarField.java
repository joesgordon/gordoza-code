package org.jutils.ui.calendar;

import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;

import org.jutils.IconConstants;
import org.jutils.Utils;
import org.jutils.ui.UFormattedTextField;
import org.jutils.ui.event.updater.DataUpdaterList;
import org.jutils.ui.event.updater.IDataUpdater;

/*******************************************************************************
 *
 ******************************************************************************/
public class CalendarField extends JPanel
{
    /**  */
    private final UFormattedTextField dateTextField = new UFormattedTextField();
    /**  */
    private final JButton calendarButton = new JButton();
    /**  */
    private final DataUpdaterList<Calendar> updaterList;

    /**  */
    private Calendar curDate = null;
    /**  */
    private String actionCommand = null;

    /***************************************************************************
     *
     **************************************************************************/
    public CalendarField()
    {
        this.setLayout( new GridBagLayout() );

        updaterList = new DataUpdaterList<Calendar>();

        SimpleDateFormat dateFormat = new SimpleDateFormat( "MM/dd/yyyy" );
        DateFormatter formatter = new DateFormatter( dateFormat );
        formatter.setFormat( dateFormat );

        dateTextField.setColumns( 10 );
        dateTextField.setHorizontalAlignment( SwingConstants.RIGHT );
        dateTextField.setFormatterFactory( new DefaultFormatterFactory(
            formatter ) );
        dateTextField.setMinimumSize( dateTextField.getPreferredSize() );
        dateTextField.getDocument().addDocumentListener(
            new DateListener( this, updaterList ) );

        calendarButton.setText( "" );
        calendarButton.addActionListener( new CalendarButtonListener() );
        calendarButton.setIcon( IconConstants.getIcon( IconConstants.CALENDAR_16 ) );
        calendarButton.setMargin( new Insets( 0, 0, 0, 0 ) );

        add( dateTextField, new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 0 ), 0, 0 ) );
        add( calendarButton, new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(
                0, 2, 0, 0 ), 0, 0 ) );
    }

    /***************************************************************************
     * @param updater
     **************************************************************************/
    public void addDataUpdater( IDataUpdater<Calendar> updater )
    {
        updaterList.addListener( updater );
    }

    /***************************************************************************
     * @param cal Calendar
     **************************************************************************/
    public void setDate( Calendar cal )
    {
        curDate = cal;

        if( curDate == null )
        {
            dateTextField.setText( "" );
        }
        else
        {
            dateTextField.setValue( curDate.getTime() );
        }
    }

    /***************************************************************************
     * @param text String
     **************************************************************************/
    public void setToolTipText( String text )
    {
        dateTextField.setToolTipText( text );
        calendarButton.setToolTipText( text );
    }

    /***************************************************************************
     * @param text String
     **************************************************************************/
    public void setTextFieldToolTipText( String text )
    {
        dateTextField.setToolTipText( text );
    }

    /***************************************************************************
     * @param text String
     **************************************************************************/
    public void setButtonToolTipText( String text )
    {
        calendarButton.setToolTipText( text );
    }

    /***************************************************************************
     * @return Calendar
     **************************************************************************/
    public Calendar getDate()
    {
        Calendar cal = new GregorianCalendar();
        Date date = ( Date )dateTextField.getValue();

        if( date != null )
        {
            cal.setTime( date );
        }

        return cal;
    }

    /***************************************************************************
     * @param enabled boolean
     **************************************************************************/
    public void setEnabled( boolean enabled )
    {
        super.setEnabled( enabled );
        calendarButton.setEnabled( enabled );
        dateTextField.setEnabled( enabled );
    }

    /***************************************************************************
     * @param listener ActionListener
     **************************************************************************/
    public void addActionListener( ActionListener listener )
    {
        listenerList.add( ActionListener.class, listener );
    }

    /***************************************************************************
     * @param listener ActionListener
     **************************************************************************/
    public void removeActionListener( ActionListener listener )
    {
        if( listener != null )
        {
            listenerList.remove( ActionListener.class, listener );
        }
    }

    /***************************************************************************
     * Sets the action command for this calendar field.
     * @param actionCommand The action command for this calendar field.
     **************************************************************************/
    public void setActionCommand( String actionCommand )
    {
        this.actionCommand = actionCommand;
    }

    /***************************************************************************
     * Returns the action command for this calendar field.
     * @return the action command for this calendar field
     **************************************************************************/
    public String getActionCommand()
    {
        return actionCommand;
    }

    /***************************************************************************
     * Notifies all listeners that have registered interest for notification on
     * this event type. The event instance is lazily created using the
     * <code>event</code> parameter.
     * @param event the <code>ActionEvent</code> object
     * @see EventListenerList
     **************************************************************************/
    protected void fireActionPerformed( ActionEvent event )
    {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        ActionEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for( int i = listeners.length - 2; i >= 0; i -= 2 )
        {
            if( listeners[i] == ActionListener.class )
            {
                // Lazily create the event:
                if( e == null )
                {
                    String actionCommand = event.getActionCommand();
                    if( actionCommand == null )
                    {
                        actionCommand = getActionCommand();
                    }
                    e = new ActionEvent( this, ActionEvent.ACTION_PERFORMED,
                        actionCommand, event.getWhen(), event.getModifiers() );
                }
                ( ( ActionListener )listeners[i + 1] ).actionPerformed( e );
            }
        }
    }

    /***************************************************************************
     * @param e ActionEvent
     **************************************************************************/
    public void displayDialog()
    {
        CalendarDialog dialog = new CalendarDialog(
            Utils.getComponentsFrame( this ), "Select Date", true );

        try
        {
            dateTextField.commitEdit();
        }
        catch( ParseException ex )
        {
            // ignore and initialize with last known good date.
        }
        curDate = getDate();

        dialog.addWindowListener( new DialogListener() );
        dialog.setDate( curDate );
        dialog.setLocationRelativeTo( null );
        dialog.setVisible( true );
    }

    /***************************************************************************
     * @param e WindowEvent
     **************************************************************************/
    private void dialogClosed( CalendarDialog dialog )
    {
        Calendar cal = dialog.getDate();
        if( cal != null )
        {
            setDate( cal );
            this.fireActionPerformed( new ActionEvent( this,
                ActionEvent.ACTION_PERFORMED, actionCommand ) );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class DateListener implements DocumentListener
    {
        private final CalendarField field;
        private final DataUpdaterList<Calendar> updaterList;

        public DateListener( CalendarField field,
            DataUpdaterList<Calendar> updaterList )
        {
            this.field = field;
            this.updaterList = updaterList;
        }

        @Override
        public void insertUpdate( DocumentEvent e )
        {
            updaterList.fireListeners( field.getDate() );
        }

        @Override
        public void removeUpdate( DocumentEvent e )
        {
            updaterList.fireListeners( field.getDate() );
        }

        @Override
        public void changedUpdate( DocumentEvent e )
        {
            updaterList.fireListeners( field.getDate() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class DialogListener extends WindowAdapter
    {
        public void windowClosed( WindowEvent e )
        {
            CalendarDialog dialog = ( CalendarDialog )e.getComponent();
            dialogClosed( dialog );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class CalendarButtonListener implements ActionListener
    {
        public void actionPerformed( ActionEvent e )
        {
            displayDialog();
        }
    }
}
