package org.jutils.ui.calendar;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.ui.app.FrameRunner;
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
        FrameRunner.invokeLater( new CalDisApp() );
    }

    /***************************************************************************
     * Defines the {@link IFrameApp} used to create and display this
     * applications UI.
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

            // frame.setSize( 500, 500 );

            SwingUtils.createTrayIcon(
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
