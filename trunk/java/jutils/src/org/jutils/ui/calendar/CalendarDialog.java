package org.jutils.ui.calendar;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.*;

import org.jutils.Utils;

//TODO comments

/*******************************************************************************
 *
 ******************************************************************************/
public class CalendarDialog extends JDialog
{
    /**  */
    private CalendarPanel calendarPanel = new CalendarPanel();
    /**  */
    private boolean okPressed = false;

    /***************************************************************************
     *
     **************************************************************************/
    public CalendarDialog()
    {
        this( new Frame(), "CalendarDialog", false );
    }

    /***************************************************************************
     * @param owner Frame
     **************************************************************************/
    public CalendarDialog( Frame owner )
    {
        this( owner, "CalendarDialog", true );
    }

    /***************************************************************************
     * @param owner Frame
     * @param modal boolean
     **************************************************************************/
    public CalendarDialog( Frame owner, boolean modal )
    {
        this( owner, "CalendarDialog", modal );
    }

    /***************************************************************************
     * @param owner Frame
     * @param title String
     * @param modal boolean
     **************************************************************************/
    public CalendarDialog( Frame owner, String title, boolean modal )
    {
        super( owner, title, modal );
        setDefaultCloseOperation( DISPOSE_ON_CLOSE );

        // ---------------------------------------------------------------------
        // Setup content panel.
        // ---------------------------------------------------------------------
        JPanel contentPanel = ( JPanel )this.getContentPane();
        contentPanel.setLayout( new GridBagLayout() );

        contentPanel.add( calendarPanel.getView(), new GridBagConstraints( 0,
            0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
            GridBagConstraints.NONE, new Insets( 4, 10, 4, 10 ), 0, 0 ) );
        contentPanel.add( createButtonPanel(), new GridBagConstraints( 0, 1, 1,
            1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 4, 4, 4, 4 ), 0, 0 ) );

        pack();
    }

    private JPanel createButtonPanel()
    {
        JPanel buttonPanel = new JPanel( new GridBagLayout() );

        JButton okButton = new JButton( "OK" );
        okButton.addActionListener( new ButtonListener( true ) );
        okButton.setDefaultCapable( true );

        JButton cancelButton = new JButton( "Cancel" );
        cancelButton.addActionListener( new ButtonListener( false ) );

        Dimension max = Utils.getMaxComponentSize( okButton, cancelButton );

        okButton.setPreferredSize( max );
        cancelButton.setPreferredSize( max );

        buttonPanel.add( okButton, new GridBagConstraints( 0, 0, 1, 1, 0.0,
            0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 0, 0, 0, 8 ), 8, 0 ) );

        buttonPanel.add( cancelButton, new GridBagConstraints( 1, 0, 1, 1, 0.0,
            0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets( 0, 8, 0, 0 ), 8, 0 ) );

        return buttonPanel;
    }

    /***************************************************************************
     * @param cal Calendar
     **************************************************************************/
    public void setDate( Calendar cal )
    {
        calendarPanel.setDate( cal );
    }

    /***************************************************************************
     * @return Calendar
     **************************************************************************/
    public Calendar getDate()
    {
        return okPressed ? calendarPanel.getDate() : null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class ButtonListener implements ActionListener
    {
        private boolean okp;

        public ButtonListener( boolean ok )
        {
            okp = ok;
        }

        public void actionPerformed( ActionEvent e )
        {
            okPressed = okp;
            dispose();
        }
    }
}
