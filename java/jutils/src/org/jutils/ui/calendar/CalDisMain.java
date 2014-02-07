package org.jutils.ui.calendar;

import javax.swing.*;

import org.junit.runner.Runner;
import org.jutils.IconConstants;
import org.jutils.ui.FrameRunner;

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
        SwingUtilities.invokeLater( new CalDisRunner() );
    }

    /***************************************************************************
     * Defines the {@link Runner} used to create and display this applications
     * UI.
     **************************************************************************/
    public static class CalDisRunner extends FrameRunner
    {
        public CalDisRunner()
        {
            ;
        }

        @Override
        protected JFrame createFrame()
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

            createTrayIcon(
                IconConstants.loader.getImage( IconConstants.CALENDAR_16 ),
                "CalDis", frame, null );

            return frame;
        }

        @Override
        protected boolean validate()
        {
            return false;
        };
    }
}
