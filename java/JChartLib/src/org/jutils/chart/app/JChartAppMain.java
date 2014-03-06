package org.jutils.chart.app;

import org.jutils.ui.app.FrameApplication;

public class JChartAppMain
{
    public static void main( String [] args )
    {
        JChartAppRunner runnable = new JChartAppRunner();

        FrameApplication.invokeLater( runnable );
    }
}
