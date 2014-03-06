package org.jutils.chart.app;

import javax.swing.JFrame;

import org.jutils.chart.ui.JChartFrameView;
import org.jutils.ui.app.IFrameApp;

public class JChartAppRunner implements IFrameApp
{
    @Override
    public JFrame createFrame()
    {
        JChartFrameView view = new JChartFrameView( JChartAppConstants.APP_NAME );
        JFrame frame = view.getView();

        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        frame.setSize( 700, 700 );

        return frame;
    }

    @Override
    public void finalizeGui()
    {
    }
}
