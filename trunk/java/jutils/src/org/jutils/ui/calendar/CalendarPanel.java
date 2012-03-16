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

import org.jutils.ui.USpinner;
import org.jutils.ui.model.CyclingSpinnerListModel;

/*******************************************************************************
 *
 ******************************************************************************/
public class CalendarPanel extends JPanel
{
    /**  */
    private final String[] MONTHS = new String[] { "January", "February",
        "March", "April", "May", "June", "July", "August", "September",
        "October", "November", "December" };

    /**  */
    private final Color headerBG = new Color( 0x8D, 0x8D, 0x8D );

    /**  */
    private final Color headerFG = new Color( 0xFF, 0xFF, 0xFF );

    /**  */
    private GridBagLayout mainLayout = new GridBagLayout();

    /**  */
    private JPanel controlsPanel = new JPanel();

    /**  */
    private GridBagLayout controlsLayout = new GridBagLayout();

    /**  */
    private USpinner timeSpinner = new USpinner( new SpinnerDateModel() );

    /**  */
    private USpinner monthSpinner = new USpinner( new CyclingSpinnerListModel(
        MONTHS ) );

    /**  */
    private USpinner yearSpinner = new USpinner( new SpinnerNumberModel() );

    /**  */
    private JPanel monthPanel = new JPanel();

    /**  */
    private GridBagLayout monthLayout = new GridBagLayout();

    /**  */
    private JLabel sundayLabel = new JLabel();

    /**  */
    private JLabel mondayLabel = new JLabel();

    /**  */
    private JLabel tuesdayLabel = new JLabel();

    /**  */
    private JLabel wednesdayLabel = new JLabel();

    /**  */
    private JLabel thursdayLabel = new JLabel();

    /**  */
    private JLabel fridayLabel = new JLabel();

    /**  */
    private JLabel saturdayLabel = new JLabel();

    /**  */
    private DayLabel[] dayLabels = new DayLabel[42];

    /**  */
    private DayLabel currentSelection = null;

    /**  */
    private boolean showTime = false;

    /***************************************************************************
     *
     **************************************************************************/
    public CalendarPanel()
    {
        this( false, null );
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
        try
        {
            this.showTime = showTime;
            jbInit();
            setDate( cal );
        }
        catch( Exception exception )
        {
            exception.printStackTrace();
        }
    }

    /***************************************************************************
     * @param label JLabel
     * @param text String
     **************************************************************************/
    private void initHeaderLabel( JLabel label, String text )
    {
        label.setForeground( headerFG );
        label.setBackground( headerBG );
        label.setHorizontalAlignment( JLabel.CENTER );
        // label.setBorder( BorderFactory.createLineBorder( Color.red ) );

        initLabel( label, text );
    }

    /***************************************************************************
     * @param label JLabel
     * @param text String
     **************************************************************************/
    private void initDayLabel( DayLabel label, String text )
    {
        label.setHorizontalAlignment( JLabel.CENTER );
        label.addMouseListener( new CalendarPanel_dayLabel_mouseAdapter( this ) );
        label.addKeyListener( new CalendarPanel_dayLabel_keyAdapter( this ) );

        initLabel( label, text );
    }

    /***************************************************************************
     * @param label JLabel
     * @param text String
     **************************************************************************/
    private void initLabel( JLabel label, String text )
    {
        label.setText( text );
        label.setOpaque( true );
        label.setPreferredSize( new Dimension( 20, 20 ) );
        label.setMinimumSize( new Dimension( 20, 20 ) );
    }

    /***************************************************************************
     *
     **************************************************************************/
    private void initLabels()
    {
        for( int i = 0; i < dayLabels.length; i++ )
        {
            dayLabels[i] = new DayLabel();
            initDayLabel( dayLabels[i], "" );
        }
    }

    /***************************************************************************
     * @throws Exception
     **************************************************************************/
    private void jbInit() throws Exception
    {
        // ----------------------------------------------------------------------
        //
        // ----------------------------------------------------------------------
        controlsPanel.setLayout( controlsLayout );

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
        yearSpinner.addChangeListener( new CalendarPanel_yearSpinner_changeAdapter(
            this ) );

        monthSpinner.addChangeListener( new CalendarPanel_monthSpinner_changeAdapter(
            this ) );
        CyclingSpinnerListModel monthModel = ( CyclingSpinnerListModel )monthSpinner.getModel();
        monthModel.setLinkedModel( yearSpinner.getModel() );

        if( showTime )
        {
            controlsPanel.add( timeSpinner, new GridBagConstraints( 0, 0, 2, 1,
                0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets( 2, 0, 2, 0 ), 0, 0 ) );
        }

        controlsPanel.add( monthSpinner, new GridBagConstraints( 0, 1, 1, 1,
            1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( showTime ? 2 : 0, 0, 0, 2 ), 0, 0 ) );
        controlsPanel.add( yearSpinner, new GridBagConstraints( 1, 1, 1, 1,
            0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( showTime ? 2 : 0, 2, 0, 0 ), 15, 0 ) );

        // ----------------------------------------------------------------------
        //
        // ----------------------------------------------------------------------
        monthPanel.setLayout( monthLayout );

        monthPanel.setBorder( BorderFactory.createLoweredBevelBorder() );

        monthPanel.setBackground( Color.white );

        initHeaderLabel( sundayLabel, "S" );
        initHeaderLabel( mondayLabel, "M" );
        initHeaderLabel( tuesdayLabel, "T" );
        initHeaderLabel( wednesdayLabel, "W" );
        initHeaderLabel( thursdayLabel, "R" );
        initHeaderLabel( fridayLabel, "F" );
        initHeaderLabel( saturdayLabel, "S" );

        initLabels();

        monthPanel.add( sundayLabel, new GridBagConstraints( 0, 0, 1, 1, 0.0,
            0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 5, 5, 0, 0 ), 0, 0 ) );
        monthPanel.add( sundayLabel, new GridBagConstraints( 0, 0, 1, 1, 0.0,
            0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 5, 5, 0, 0 ), 0, 0 ) );
        monthPanel.add( mondayLabel, new GridBagConstraints( 1, 0, 1, 1, 0.0,
            0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 5, 0, 0, 0 ), 0, 0 ) );
        monthPanel.add( tuesdayLabel, new GridBagConstraints( 2, 0, 1, 1, 0.0,
            0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 5, 0, 0, 0 ), 0, 0 ) );
        monthPanel.add( wednesdayLabel, new GridBagConstraints( 3, 0, 1, 1,
            0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 5, 0, 0, 0 ), 0, 0 ) );
        monthPanel.add( thursdayLabel, new GridBagConstraints( 4, 0, 1, 1, 0.0,
            0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 5, 0, 0, 0 ), 0, 0 ) );
        monthPanel.add( fridayLabel, new GridBagConstraints( 5, 0, 1, 1, 0.0,
            0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 5, 0, 0, 0 ), 0, 0 ) );
        monthPanel.add( saturdayLabel, new GridBagConstraints( 6, 0, 1, 1, 0.0,
            0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 5, 0, 0, 5 ), 0, 0 ) );

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

        // ----------------------------------------------------------------------
        //
        // ----------------------------------------------------------------------
        this.setLayout( mainLayout );

        this.add( controlsPanel, new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets( 0, 0, 2, 0 ), 0, 0 ) );

        this.add( monthPanel, new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets( 2,
                0, 0, 0 ), 0, 0 ) );

        this.setMinimumSize( this.getPreferredSize() );
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

        int lastDayPrevious = lastMonth.getActualMaximum( Calendar.DAY_OF_MONTH );

        for( int i = 0; i < dayLabels.length; i++ )
        {
            if( i < firstIndex )
            {
                dayLabels[i].setText( "" +
                    ( lastDayPrevious - firstIndex + i + 1 ) );
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
     * @param e MouseEvent
     **************************************************************************/
    void dayLabel_mouseClicked( MouseEvent e )
    {
        DayLabel newCurrent = ( DayLabel )e.getComponent();

        if( newCurrent != currentSelection )
        {
            currentSelection.setSelected( false );
            if( newCurrent.isNonDay() )
            {
                Calendar cal = newCurrent.getDate();
                Date dateTime = ( Date )timeSpinner.getValue();
                Calendar time = new GregorianCalendar();
                time.setTime( dateTime );
                cal.set( Calendar.HOUR, time.get( Calendar.HOUR ) );
                cal.set( Calendar.MINUTE, time.get( Calendar.MINUTE ) );
                cal.set( Calendar.SECOND, time.get( Calendar.SECOND ) );

                this.setDate( cal );
            }
            else
            {
                newCurrent.setSelected( true );

                currentSelection = newCurrent;
            }
        }

        newCurrent.requestFocus();
    }

    /***************************************************************************
     * @param e ActionEvent
     **************************************************************************/
    void monthSpinner_stateChanged( ChangeEvent e )
    {
        int month = getMonthIndex( monthSpinner.getValue().toString() );
        int day = currentSelection.getDay();
        int year = ( ( Number )yearSpinner.getValue() ).intValue();

        Date dateTime = ( Date )timeSpinner.getValue();
        Calendar time = new GregorianCalendar();
        time.setTime( dateTime );

        this.setDate( new GregorianCalendar( year, month, day,
            time.get( Calendar.HOUR ), time.get( Calendar.MINUTE ),
            time.get( Calendar.SECOND ) ) );
    }

    /***************************************************************************
     * @param mth String
     * @return int
     **************************************************************************/
    private int getMonthIndex( String mth )
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
     * @param e ChangeEvent
     **************************************************************************/
    void yearSpinner_stateChanged( ChangeEvent e )
    {
        int month = getMonthIndex( monthSpinner.getValue().toString() );
        int day = currentSelection.getDay();
        int year = ( ( Number )yearSpinner.getValue() ).intValue();

        Date dateTime = ( Date )timeSpinner.getValue();
        Calendar time = new GregorianCalendar();
        time.setTime( dateTime );

        this.setDate( new GregorianCalendar( year, month, day,
            time.get( Calendar.HOUR ), time.get( Calendar.MINUTE ),
            time.get( Calendar.SECOND ) ) );
    }

    /***************************************************************************
     * @param cal Calendar
     **************************************************************************/
    public static void debugPrintCal( Calendar cal )
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
            "MM/dd/yyyy hh:mm:ss a" );

        System.out.println( dateFormat.format( cal.getTime() ) );
    }

    /***************************************************************************
     * @param e KeyEvent
     **************************************************************************/
    void dayLabel_keyPressed( KeyEvent e )
    {
        if( e.getKeyCode() == KeyEvent.VK_UP )
        {
            Calendar cal = this.getDate();
            cal.add( Calendar.DATE, -7 );
            this.setDate( cal );
            this.currentSelection.requestFocus();
        }
        else if( e.getKeyCode() == KeyEvent.VK_DOWN )
        {
            Calendar cal = this.getDate();
            cal.add( Calendar.DATE, 7 );
            this.setDate( cal );
            this.currentSelection.requestFocus();
        }
        else if( e.getKeyCode() == KeyEvent.VK_LEFT )
        {
            Calendar cal = this.getDate();
            cal.add( Calendar.DATE, -1 );
            this.setDate( cal );
            this.currentSelection.requestFocus();
        }
        else if( e.getKeyCode() == KeyEvent.VK_RIGHT )
        {
            Calendar cal = this.getDate();
            cal.add( Calendar.DATE, 1 );
            this.setDate( cal );
            this.currentSelection.requestFocus();
        }
    }
}

class CalendarPanel_dayLabel_keyAdapter extends KeyAdapter
{
    private CalendarPanel adaptee;

    CalendarPanel_dayLabel_keyAdapter( CalendarPanel adaptee )
    {
        this.adaptee = adaptee;
    }

    public void keyPressed( KeyEvent e )
    {
        adaptee.dayLabel_keyPressed( e );
    }
}

class CalendarPanel_yearSpinner_changeAdapter implements ChangeListener
{
    private CalendarPanel adaptee;

    CalendarPanel_yearSpinner_changeAdapter( CalendarPanel adaptee )
    {
        this.adaptee = adaptee;
    }

    public void stateChanged( ChangeEvent e )
    {
        adaptee.yearSpinner_stateChanged( e );
    }
}

class CalendarPanel_monthSpinner_changeAdapter implements ChangeListener
{
    private CalendarPanel adaptee;

    CalendarPanel_monthSpinner_changeAdapter( CalendarPanel adaptee )
    {
        this.adaptee = adaptee;
    }

    public void stateChanged( ChangeEvent e )
    {
        adaptee.monthSpinner_stateChanged( e );
    }
}

class CalendarPanel_dayLabel_mouseAdapter extends MouseAdapter
{
    private CalendarPanel adaptee;

    CalendarPanel_dayLabel_mouseAdapter( CalendarPanel adaptee )
    {
        this.adaptee = adaptee;
    }

    public void mouseClicked( MouseEvent e )
    {
        adaptee.dayLabel_mouseClicked( e );
    }
}
