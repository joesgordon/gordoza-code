package org.jutils.ui.calendar;

import java.awt.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jutils.ui.FrameRunner;
import org.jutils.ui.USpinner;
import org.jutils.ui.event.updater.DataUpdaterList;
import org.jutils.ui.event.updater.IDataUpdater;

/*******************************************************************************
 * 
 ******************************************************************************/
public class DateTimePanel extends JPanel
{
    /**  */
    private SpinnerDateModel timeModel;
    /**  */
    private CalendarField dateField;
    /**  */
    private DataUpdaterList<Calendar> updaterList;

    /***************************************************************************
     * 
     **************************************************************************/
    public DateTimePanel()
    {
        super.setLayout( new GridBagLayout() );

        updaterList = new DataUpdaterList<Calendar>();
        timeModel = new SpinnerDateModel( new Date(), null, null,
            Calendar.AM_PM );
        JSpinner spinner = new USpinner( timeModel );
        JSpinner.DateEditor spinnerEditor = new JSpinner.DateEditor( spinner,
            "hh:mm:ss a" );
        spinner.setEditor( spinnerEditor );

        dateField = new CalendarField();

        spinner.addChangeListener( new TimeListener( this, updaterList ) );

        add( spinner, new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 2 ), 0, 0 ) );
        add( dateField, new GridBagConstraints( 1, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 2, 0, 0 ), 0, 0 ) );
    }

    /***************************************************************************
     * @param updater
     **************************************************************************/
    public void addDataUpdater( IDataUpdater<Calendar> updater )
    {
        updaterList.addListener( updater );
        dateField.addDataUpdater( updater );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Calendar getDate()
    {
        Calendar cal = dateField.getDate();
        Calendar timeCal = new GregorianCalendar();
        timeCal.setTime( timeModel.getDate() );

        cal.set( Calendar.HOUR_OF_DAY, timeCal.get( Calendar.HOUR_OF_DAY ) );
        cal.set( Calendar.MINUTE, timeCal.get( Calendar.MINUTE ) );
        cal.set( Calendar.SECOND, timeCal.get( Calendar.SECOND ) );

        return cal;
    }

    /***************************************************************************
     * @param cal
     **************************************************************************/
    public void setDate( Calendar cal )
    {
        timeModel.setValue( cal.getTime() );
        dateField.setDate( cal );
    }

    private static class TimeListener implements ChangeListener
    {
        private final DateTimePanel field;
        private final DataUpdaterList<Calendar> updaterList;

        public TimeListener( DateTimePanel field,
            DataUpdaterList<Calendar> updaterList )
        {
            this.field = field;
            this.updaterList = updaterList;
        }

        @Override
        public void stateChanged( ChangeEvent e )
        {
            updaterList.fireListeners( field.getDate() );
        }
    }

    /***************************************************************************
     * The main function.
     * @param args Unused arguments.
     **************************************************************************/
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( new Runner() );
    }
}

class Runner extends FrameRunner
{
    @Override
    protected JFrame createFrame()
    {
        DateTimePanel p = new DateTimePanel();
        p.setDate( new GregorianCalendar() );

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setContentPane( p );
        frame.setSize( 300, 300 );

        return frame;
    }

    @Override
    protected boolean validate()
    {
        return true;
    }
}
