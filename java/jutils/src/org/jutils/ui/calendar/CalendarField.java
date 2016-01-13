package org.jutils.ui.calendar;

import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.ui.event.updater.DataUpdaterList;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.model.IView;

/*******************************************************************************
 *
 ******************************************************************************/
public class CalendarField implements IView<JPanel>
{
    /**  */
    private final JPanel view;
    /**  */
    private final JFormattedTextField dateTextField = new JFormattedTextField();
    /**  */
    private final JButton calendarButton = new JButton();
    /**  */
    private final DataUpdaterList<Calendar> updaterList;

    /**  */
    private Calendar curDate = null;

    /***************************************************************************
     *
     **************************************************************************/
    public CalendarField()
    {
        this.view = new JPanel( new GridBagLayout() );

        updaterList = new DataUpdaterList<Calendar>();

        SimpleDateFormat dateFormat = new SimpleDateFormat( "MM/dd/yyyy" );
        DateFormatter formatter = new DateFormatter( dateFormat );
        formatter.setFormat( dateFormat );

        dateTextField.setColumns( 10 );
        dateTextField.setHorizontalAlignment( SwingConstants.RIGHT );
        dateTextField.setFormatterFactory(
            new DefaultFormatterFactory( formatter ) );
        dateTextField.setMinimumSize( dateTextField.getPreferredSize() );
        dateTextField.getDocument().addDocumentListener(
            new DateListener( this, updaterList ) );

        calendarButton.setText( "" );
        calendarButton.addActionListener( new CalendarButtonListener() );
        calendarButton.setIcon(
            IconConstants.loader.getIcon( IconConstants.CALENDAR_16 ) );
        calendarButton.setMargin( new Insets( 0, 0, 0, 0 ) );

        view.add( dateTextField,
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets( 0, 0, 0, 0 ), 0, 0 ) );
        view.add( calendarButton,
            new GridBagConstraints( 1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                new Insets( 0, 2, 0, 0 ), 0, 0 ) );

        setDate( new GregorianCalendar() );
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
     * @param updater
     **************************************************************************/
    public void addDataUpdater( IUpdater<Calendar> updater )
    {
        updaterList.addListener( updater );
    }

    /***************************************************************************
     * @param millis
     **************************************************************************/
    public void setMillis( long millis )
    {
        GregorianCalendar cal = new GregorianCalendar();

        cal.setTimeInMillis( millis );

        setDate( cal );
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
        Calendar cal = new GregorianCalendar( this.curDate.getTimeZone() );
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
        view.setEnabled( enabled );
        calendarButton.setEnabled( enabled );
        dateTextField.setEnabled( enabled );
    }

    /***************************************************************************
     * @param e ActionEvent
     **************************************************************************/
    public void displayDialog()
    {
        CalendarDialog dialog = new CalendarDialog(
            SwingUtils.getComponentsFrame( view ), "Select Date", true );

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
