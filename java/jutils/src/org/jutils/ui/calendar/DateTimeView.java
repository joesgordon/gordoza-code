package org.jutils.ui.calendar;

import java.awt.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jutils.ui.SpinnerWheelListener;
import org.jutils.ui.app.FrameApplication;
import org.jutils.ui.app.IFrameApp;
import org.jutils.ui.event.updater.DataUpdaterList;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.model.IDataView;

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

    /**  */
    private final TimeListener timeChanged;

    /***************************************************************************
     * 
     **************************************************************************/
    public DateTimeView()
    {
        this.view = new JPanel( new GridBagLayout() );

        this.updaterList = new DataUpdaterList<Calendar>();
        this.timeModel = new SpinnerDateModel( new Date(), null, null,
            Calendar.AM_PM );
        JSpinner spinner = new JSpinner( timeModel );
        JSpinner.DateEditor spinnerEditor = new JSpinner.DateEditor( spinner,
            "hh:mm:ss a" );
        spinner.setEditor( spinnerEditor );

        SpinnerWheelListener.install( spinner );

        this.dateField = new CalendarField();
        this.timeChanged = new TimeListener( this, updaterList );

        spinner.addChangeListener( timeChanged );

        view.add( spinner,
            new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets( 0, 0, 0, 2 ), 0, 0 ) );
        view.add( dateField.getView(),
            new GridBagConstraints( 1, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
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
        timeChanged.setEnabled( false );
        timeModel.setValue( cal.getTime() );
        dateField.setDate( cal );
        timeChanged.setEnabled( true );
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

        private boolean enabled;

        public TimeListener( DateTimeView field,
            DataUpdaterList<Calendar> updaterList )
        {
            this.field = field;
            this.updaterList = updaterList;

            this.enabled = true;
        }

        @Override
        public void stateChanged( ChangeEvent e )
        {
            if( enabled )
            {
                updaterList.fireListeners( field.getDate() );
            }
        }

        public void setEnabled( boolean enabled )
        {
            this.enabled = enabled;
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
