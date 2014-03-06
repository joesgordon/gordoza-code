package org.jutils.chart.app;

import org.jutils.ui.app.FrameApplication;

public class JChartAppMain
{
    public static void main( String [] args )
    {
        JChartApp runnable = new JChartApp();

        FrameApplication.invokeLater( runnable );
    }
}
