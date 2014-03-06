package org.jutils.ui.calendar;

import javax.swing.*;

import org.junit.runner.Runner;
import org.jutils.IconConstants;
import org.jutils.ui.app.FrameApplication;
import org.jutils.ui.app.IFrameApp;

/*******************************************************************************
 * Defines an application that simply displays a calendar.
 ******************************************************************************/
public class CalDisMain
{
    /***************************************************************************
     * Defines the main entry point for this application. Arguments are ignored.
     * @param args ignored.
     **************************************************************************/
    public static void main( String [] args )
    {
        FrameApplication.invokeLater( new CalDisApp() );
    }

    /***************************************************************************
     * Defines the {@link Runner} used to create and display this applications
     * UI.
     **************************************************************************/
    public static class CalDisApp implements IFrameApp
    {
        @Override
        public JFrame createFrame()
        {
            JFrame frame = new JFrame();
            CalendarPanel calView = new CalendarPanel();
            JPanel panel = new JPanel();

            panel.add( calView.getView() );

            frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            frame.setTitle( "CalDis" );
            frame.setContentPane( panel );

            // TODO Auto-generated method stub

            // frame.setSize( 500, 500 );

            FrameApplication.createTrayIcon(
                IconConstants.loader.getImage( IconConstants.CALENDAR_16 ),
                "CalDis", frame, null );

            return frame;
        }

        @Override
        public void finalizeGui()
        {
        }
    }
}
