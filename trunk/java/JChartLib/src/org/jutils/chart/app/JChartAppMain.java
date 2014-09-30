package org.jutils.chart.app;

import org.jutils.ui.app.FrameApplication;

/*******************************************************************************
 * 
 ******************************************************************************/
public class JChartAppMain
{
    /***************************************************************************
     * 
     **************************************************************************/
    private JChartAppMain()
    {
    }

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        JChartApp runnable = new JChartApp( JChartAppConstants.getUserIO() );

        FrameApplication.invokeLater( runnable );
    }
}
