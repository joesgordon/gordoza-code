package org.jutils.chart.app;

import javax.swing.SwingUtilities;

public class JChartAppMain
{
    public static void main( String[] args )
    {
        JChartAppRunner runnable = new JChartAppRunner();

        SwingUtilities.invokeLater( runnable );
    }
}
