package org.jutils.ui.calendar;

import java.awt.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jutils.ui.AutoSpinner;
import org.jutils.ui.app.FrameApplication;
import org.jutils.ui.app.IFrameApp;
import org.jutils.ui.event.updater.DataUpdaterList;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.model.IDataView;

//TODO comments

/*******************************************************************************
 * 
 ******************************************************************************/
public class DateTimeView implements IDataView<Calendar>
{
    /**  */
    private final SpinnerDateModel timeModel;
    /**  */
    private final CalendarField dateField;
    /**  */
    private final DataUpdaterList<Calendar> updaterList;
    /**  */
    private final JPanel view;

    /***************************************************************************
     * 
     **************************************************************************/
    public DateTimeView()
    {
        view = new JPanel( new GridBagLayout() );

        updaterList = new DataUpdaterList<Calendar>();
        timeModel = new SpinnerDateModel( new Date(), null, null,
            Calendar.AM_PM );
        JSpinner spinner = new AutoSpinner( timeModel );
        JSpinner.DateEditor spinnerEditor = new JSpinner.DateEditor( spinner,
            "hh:mm:ss a" );
        spinner.setEditor( spinnerEditor );

        dateField = new CalendarField();

        spinner.addChangeListener( new TimeListener( this, updaterList ) );

        view.add( spinner, new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 0, 2 ), 0, 0 ) );
        view.add( dateField.getView(), new GridBagConstraints( 1, 0, 1, 1, 1.0,
            1.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 2, 0, 0 ), 0, 0 ) );
    }

    /***************************************************************************
     * @param updater
     **************************************************************************/
    public void addDataUpdater( IUpdater<Calendar> updater )
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

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Component getView()
    {
        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Calendar getData()
    {
        return getDate();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( Calendar data )
    {
        setDate( data );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class TimeListener implements ChangeListener
    {
        private final DateTimeView field;
        private final DataUpdaterList<Calendar> updaterList;

        public TimeListener( DateTimeView field,
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
     * 
     **************************************************************************/
    private static class DateTimeViewApp implements IFrameApp
    {
        @Override
        public JFrame createFrame()
        {
            DateTimeView p = new DateTimeView();
            p.setDate( new GregorianCalendar() );

            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            frame.setContentPane( ( Container )p.getView() );
            frame.setSize( 300, 300 );

            return frame;
        }

        @Override
        public void finalizeGui()
        {
        }
    }

    /***************************************************************************
     * The main function.
     * @param args Unused arguments.
     **************************************************************************/
    public static void main( String [] args )
    {
        FrameApplication.invokeLater( new DateTimeViewApp() );
    }
}
