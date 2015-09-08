package org.jutils.ui.calendar;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;

import org.jutils.io.LogUtils;
import org.jutils.ui.SpinnerWheelListener;
import org.jutils.ui.event.ItemActionList;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.model.CyclingSpinnerListModel;
import org.jutils.ui.model.IDataView;

/*******************************************************************************
 *
 ******************************************************************************/
public class CalendarPanel implements IDataView<Long>
{
    /** Month strings to be used in the months' spinner box. */
    private static final String [] MONTHS = new String[] { "January",
        "February", "March", "April", "May", "June", "July", "August",
        "September", "October", "November", "December" };
    /**
     * Week day single letter abbreviations to be used as the header in the
     * calendar.
     */
    private static final String [] WEEK_DAYS = new String[] { "S", "M", "T",
        "W", "R", "F", "S" };
    /** The background of the header labels. */
    private static final Color HEADER_BACKGROUND = new Color( 0x8D, 0x8D,
        0x8D );
    /** The foreground of the header labels. */
    private static final Color HEADER_FOREGROUND = new Color( 0xFF, 0xFF,
        0xFF );

    /**  */
    private final JPanel view;
    /**  */
    private final JSpinner timeSpinner;
    /**  */
    private final JSpinner monthSpinner;
    /**  */
    private final JSpinner yearSpinner;
    /**  */
    private final JLabel [] weekdayLabels;
    /**  */
    private final DayLabel [] dayLabels;
    /**  */
    private final ItemActionList<Long> dateChangedListeners;

    /**  */
    private DayLabel currentSelection = null;
    /**  */
    private boolean showTime = false;

    /***************************************************************************
     *
     **************************************************************************/
    public CalendarPanel()
    {
        this( false );
    }

    /***************************************************************************
     * @param showTime boolean
     **************************************************************************/
    public CalendarPanel( boolean showTime )
    {
        this( showTime, null );
    }

    /***************************************************************************
     * @param cal Calendar
     **************************************************************************/
    public CalendarPanel( Calendar cal )
    {
        this( false, cal );
    }

    /***************************************************************************
     * @param showTime boolean
     * @param cal Calendar
     **************************************************************************/
    public CalendarPanel( boolean showTime, Calendar cal )
    {
        this.showTime = showTime;

        this.timeSpinner = new JSpinner( new SpinnerDateModel() );
        this.monthSpinner = new JSpinner(
            new CyclingSpinnerListModel( MONTHS ) );
        this.yearSpinner = new JSpinner( new SpinnerNumberModel() );
        this.weekdayLabels = new JLabel[WEEK_DAYS.length];
        this.dayLabels = new DayLabel[42];

        this.dateChangedListeners = new ItemActionList<Long>();

        SpinnerWheelListener.install( timeSpinner );
        SpinnerWheelListener.install( monthSpinner );
        SpinnerWheelListener.install( yearSpinner );

        for( int i = 0; i < weekdayLabels.length; i++ )
        {
            weekdayLabels[i] = new JLabel( WEEK_DAYS[i] );
            initHeaderLabel( weekdayLabels[i] );
        }

        for( int i = 0; i < dayLabels.length; i++ )
        {
            dayLabels[i] = new DayLabel();
            initDayLabel( dayLabels[i] );
        }

        this.view = createView();

        setDate( cal );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Long getData()
    {
        return getDate().getTimeInMillis();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( Long data )
    {
        Calendar cal = new GregorianCalendar();
        if( data != null )
        {
            cal.setTimeInMillis( data );
        }
        setDate( cal );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addDateChangedListener( ItemActionListener<Long> l )
    {
        dateChangedListeners.addListener( l );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        // ---------------------------------------------------------------------
        //
        // ---------------------------------------------------------------------
        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 2, 0 ), 0, 0 );
        panel.add( createControlsPanel(), constraints );

        // ---------------------------------------------------------------------
        //
        // ---------------------------------------------------------------------
        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 2, 0, 0, 0 ), 0, 0 );
        panel.add( createMonthPanel(), constraints );

        panel.setMinimumSize( panel.getPreferredSize() );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createControlsPanel()
    {
        JPanel panel = new JPanel( new GridBagLayout() );

        SimpleDateFormat dateFormat = new SimpleDateFormat( "hh:mm:ss a" );

        JFormattedTextField timeTextField = ( ( JSpinner.DefaultEditor )timeSpinner.getEditor() ).getTextField();
        timeTextField.setHorizontalAlignment( SwingConstants.RIGHT );
        DefaultFormatterFactory factory = ( DefaultFormatterFactory )timeTextField.getFormatterFactory();
        DateFormatter formatter = ( DateFormatter )factory.getDefaultFormatter();
        formatter.setFormat( dateFormat );
        timeTextField.setMargin( new Insets( 0, 5, 0, 0 ) );
        timeTextField.setColumns( 5 );

        JSpinner.NumberEditor editor = new JSpinner.NumberEditor( yearSpinner,
            "#" );
        yearSpinner.setEditor( editor );
        yearSpinner.addChangeListener( new YearChangeListener( this ) );

        monthSpinner.addChangeListener( new MonthChangeListener( this ) );
        CyclingSpinnerListModel monthModel = ( CyclingSpinnerListModel )monthSpinner.getModel();
        monthModel.setLinkedModel( yearSpinner.getModel() );

        if( showTime )
        {
            panel.add( timeSpinner,
                new GridBagConstraints( 0, 0, 2, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets( 2, 0, 2, 0 ), 0, 0 ) );
        }

        panel.add( monthSpinner,
            new GridBagConstraints( 0, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( showTime ? 2 : 0, 0, 0, 2 ), 0, 0 ) );
        panel.add( yearSpinner,
            new GridBagConstraints( 1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( showTime ? 2 : 0, 2, 0, 0 ), 15, 0 ) );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Component createMonthPanel()
    {
        JPanel monthPanel = new JPanel( new GridBagLayout() );

        monthPanel.setBorder( BorderFactory.createLoweredBevelBorder() );

        monthPanel.setBackground( Color.white );

        for( int i = 0; i < weekdayLabels.length; i++ )
        {
            monthPanel.add( weekdayLabels[i], new GridBagConstraints( i, 0, 1,
                1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 5, i == 0 ? 5 : 0, 0, i == 6 ? 5 : 0 ), 0, 0 ) );
        }

        for( int i = 0; i < dayLabels.length; i++ )
        {
            int row = 1 + ( i / 7 );
            int col = i % 7;

            int top = 0;
            int lft = col == 0 ? 5 : 0;
            int btm = row == 6 ? 5 : 0;
            int rht = col == 6 ? 5 : 0;

            monthPanel.add( dayLabels[i],
                new GridBagConstraints( col, row, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.NONE,
                    new Insets( top, lft, btm, rht ), 0, 0 ) );
        }

        return monthPanel;
    }

    /***************************************************************************
     * @param label JLabel
     * @param text String
     **************************************************************************/
    private void initHeaderLabel( JLabel label )
    {
        label.setForeground( HEADER_FOREGROUND );
        label.setBackground( HEADER_BACKGROUND );
        label.setHorizontalAlignment( JLabel.CENTER );
        // label.setBorder( BorderFactory.createLineBorder( Color.red ) );

        initLabel( label );
    }

    /***************************************************************************
     * @param label JLabel
     * @param text String
     **************************************************************************/
    private void initDayLabel( DayLabel label )
    {
        label.setHorizontalAlignment( JLabel.CENTER );
        label.addMouseListener( new DayLabelMouseListener( this ) );
        label.addKeyListener( new DayLabelKeyListener( this ) );

        initLabel( label );
    }

    /***************************************************************************
     * @param label JLabel
     * @param text String
     **************************************************************************/
    private void initLabel( JLabel label )
    {
        label.setOpaque( true );
        label.setPreferredSize( new Dimension( 20, 20 ) );
        label.setMinimumSize( new Dimension( 20, 20 ) );
    }

    /***************************************************************************
     * @param cal Calendar
     **************************************************************************/
    public void setDate( Calendar cal )
    {
        if( this.currentSelection != null )
        {
            this.currentSelection.setSelected( false );
        }

        if( cal == null )
        {
            cal = new GregorianCalendar();
        }

        timeSpinner.setValue( cal.getTime() );

        GregorianCalendar lastMonth = new GregorianCalendar(
            cal.get( Calendar.YEAR ), cal.get( Calendar.MONTH ),
            cal.get( Calendar.DATE ) );
        GregorianCalendar nextMonth = new GregorianCalendar(
            cal.get( Calendar.YEAR ), cal.get( Calendar.MONTH ),
            cal.get( Calendar.DATE ) );

        lastMonth.add( Calendar.MONTH, -1 );
        nextMonth.add( Calendar.MONTH, 1 );

        int firstIndex = ( cal.get( Calendar.DAY_OF_WEEK ) -
            ( cal.get( Calendar.DAY_OF_MONTH ) % 7 ) + 7 ) % 7;
        int lastIndex = firstIndex +
            cal.getActualMaximum( Calendar.DAY_OF_MONTH ) - 1;
        int todaysIndex = firstIndex + cal.get( Calendar.DAY_OF_MONTH ) - 1;

        int lastDayPrevious = lastMonth.getActualMaximum(
            Calendar.DAY_OF_MONTH );

        for( int i = 0; i < dayLabels.length; i++ )
        {
            if( i < firstIndex )
            {
                dayLabels[i].setText(
                    "" + ( lastDayPrevious - firstIndex + i + 1 ) );
                dayLabels[i].setNonDay( true );
                dayLabels[i].setYear( lastMonth.get( Calendar.YEAR ) );
                dayLabels[i].setMonth( lastMonth.get( Calendar.MONTH ) );
            }
            else if( i > lastIndex )
            {
                dayLabels[i].setText( "" + ( i - lastIndex ) );
                dayLabels[i].setNonDay( true );
                dayLabels[i].setYear( nextMonth.get( Calendar.YEAR ) );
                dayLabels[i].setMonth( nextMonth.get( Calendar.MONTH ) );
            }
            else
            {
                dayLabels[i].setText( "" + ( i - firstIndex + 1 ) );
                dayLabels[i].setNonDay( false );

                dayLabels[i].setYear( cal.get( Calendar.YEAR ) );
                dayLabels[i].setMonth( cal.get( Calendar.MONTH ) );
                if( i == todaysIndex )
                {
                    dayLabels[i].setSelected( true );
                    currentSelection = dayLabels[i];
                }
            }
        }

        monthSpinner.setValue( MONTHS[cal.get( Calendar.MONTH )] );

        yearSpinner.setValue( new Integer( cal.get( Calendar.YEAR ) ) );
    }

    /***************************************************************************
     * @return GregorianCalendar
     **************************************************************************/
    public GregorianCalendar getDate()
    {
        GregorianCalendar gc = null;

        if( this.currentSelection != null )
        {
            gc = this.currentSelection.getDate();

            Date dateTime = ( Date )timeSpinner.getValue();
            Calendar time = new GregorianCalendar();
            time.setTime( dateTime );

            gc.set( Calendar.HOUR, time.get( Calendar.HOUR ) );
            gc.set( Calendar.MINUTE, time.get( Calendar.MINUTE ) );
            gc.set( Calendar.SECOND, time.get( Calendar.SECOND ) );
        }

        return gc;
    }

    /***************************************************************************
     * @param mth String
     * @return int
     **************************************************************************/
    private static int getMonthIndex( String mth )
    {
        for( int i = 0; i < MONTHS.length; i++ )
        {
            if( MONTHS[i].compareToIgnoreCase( mth ) == 0 )
            {
                return i;
            }
        }
        return -1;
    }

    /***************************************************************************
     * @param cal Calendar
     **************************************************************************/
    public static void debugPrintCal( Calendar cal )
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
            "MM/dd/yyyy hh:mm:ss a" );

        LogUtils.printDebug( dateFormat.format( cal.getTime() ) );
    }

    private void updateFromSpinners()
    {
        String mthStr = monthSpinner.getValue().toString();
        int month = getMonthIndex( mthStr );
        int day = currentSelection.getDay();
        int year = ( ( Number )yearSpinner.getValue() ).intValue();

        Date dateTime = ( Date )timeSpinner.getValue();
        Calendar time = new GregorianCalendar();
        time.setTime( dateTime );

        time = new GregorianCalendar( year, month, day,
            time.get( Calendar.HOUR ), time.get( Calendar.MINUTE ),
            time.get( Calendar.SECOND ) );

        setDate( time );
        dateChangedListeners.fireListeners( this, time.getTimeInMillis() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class DayLabelKeyListener extends KeyAdapter
    {
        private final CalendarPanel calPanel;

        public DayLabelKeyListener( CalendarPanel adaptee )
        {
            this.calPanel = adaptee;
        }

        public void keyPressed( KeyEvent e )
        {
            if( e.getKeyCode() == KeyEvent.VK_UP )
            {
                Calendar cal = calPanel.getDate();
                cal.add( Calendar.DATE, -7 );
                calPanel.setDate( cal );
                calPanel.currentSelection.requestFocus();
            }
            else if( e.getKeyCode() == KeyEvent.VK_DOWN )
            {
                Calendar cal = calPanel.getDate();
                cal.add( Calendar.DATE, 7 );
                calPanel.setDate( cal );
                calPanel.currentSelection.requestFocus();
            }
            else if( e.getKeyCode() == KeyEvent.VK_LEFT )
            {
                Calendar cal = calPanel.getDate();
                cal.add( Calendar.DATE, -1 );
                calPanel.setDate( cal );
                calPanel.currentSelection.requestFocus();
            }
            else if( e.getKeyCode() == KeyEvent.VK_RIGHT )
            {
                Calendar cal = calPanel.getDate();
                cal.add( Calendar.DATE, 1 );
                calPanel.setDate( cal );
                calPanel.currentSelection.requestFocus();
            }
        }

        // TODO call listeners.
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class YearChangeListener implements ChangeListener
    {
        private final CalendarPanel calPanel;

        public YearChangeListener( CalendarPanel adaptee )
        {
            this.calPanel = adaptee;
        }

        public void stateChanged( ChangeEvent e )
        {
            calPanel.updateFromSpinners();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class MonthChangeListener implements ChangeListener
    {
        private final CalendarPanel calPanel;

        public MonthChangeListener( CalendarPanel adaptee )
        {
            this.calPanel = adaptee;
        }

        public void stateChanged( ChangeEvent e )
        {
            calPanel.updateFromSpinners();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class DayLabelMouseListener extends MouseAdapter
    {
        private final CalendarPanel adaptee;

        public DayLabelMouseListener( CalendarPanel adaptee )
        {
            this.adaptee = adaptee;
        }

        public void mouseClicked( MouseEvent e )
        {
            DayLabel newCurrent = ( DayLabel )e.getComponent();

            if( newCurrent != adaptee.currentSelection )
            {
                adaptee.currentSelection.setSelected( false );
                if( newCurrent.isNonDay() )
                {
                    Calendar cal = newCurrent.getDate();
                    Date dateTime = ( Date )adaptee.timeSpinner.getValue();
                    Calendar time = new GregorianCalendar();
                    time.setTime( dateTime );
                    cal.set( Calendar.HOUR, time.get( Calendar.HOUR ) );
                    cal.set( Calendar.MINUTE, time.get( Calendar.MINUTE ) );
                    cal.set( Calendar.SECOND, time.get( Calendar.SECOND ) );

                    adaptee.setDate( cal );
                }
                else
                {
                    newCurrent.setSelected( true );

                    adaptee.currentSelection = newCurrent;
                }
            }

            newCurrent.requestFocus();

            // TODO call listeners.
        }
    }
}
