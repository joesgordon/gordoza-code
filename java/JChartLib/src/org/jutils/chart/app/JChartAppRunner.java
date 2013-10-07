package org.jutils.chart.app;

import javax.swing.JFrame;

import org.jutils.chart.ui.JChartFrameView;
import org.jutils.ui.FrameRunner;
import org.jutils.ui.StandardUncaughtExceptionHandler;

public class JChartAppRunner extends FrameRunner
{
    public JChartAppRunner()
    {
        ;
    }

    @Override
    protected JFrame createFrame()
    {
        JChartFrameView view = new JChartFrameView( JChartAppConstants.APP_NAME );
        JFrame frame = view.getView();

        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        frame.setSize( 700, 700 );

        StandardUncaughtExceptionHandler sueh = new StandardUncaughtExceptionHandler(
            frame );

        Thread.setDefaultUncaughtExceptionHandler( sueh );

        return frame;
    }

    @Override
    protected boolean validate()
    {
        return true;
    }
}
