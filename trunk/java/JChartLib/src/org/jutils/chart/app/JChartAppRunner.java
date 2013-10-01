package org.jutils.chart.app;

import javax.swing.JFrame;

import org.jutils.chart.ui.JChartFrameView;
import org.jutils.ui.FrameRunner;

public class JChartAppRunner extends FrameRunner
{
    public JChartAppRunner()
    {
        ;
    }

    @Override
    protected JFrame createFrame()
    {
        JChartFrameView view = new JChartFrameView();
        JFrame frame = view.getView();

        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        frame.setSize( 500, 500 );

        return frame;
    }

    @Override
    protected boolean validate()
    {
        return true;
    }
}
