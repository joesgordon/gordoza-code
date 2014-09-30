package org.jutils.chart.ui.objects;

import java.awt.*;

import org.jutils.chart.model.Chart;
import org.jutils.chart.ui.IChartWidget;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChartWidget implements IChartWidget
{
    /**  */
    public final PlotWidget plot;
    /**  */
    public final AxesWidget axes;

    /**  */
    public Chart chart;

    /***************************************************************************
     * 
     **************************************************************************/
    public ChartWidget()
    {
        this.plot = new PlotWidget();
        this.axes = new AxesWidget();

        this.chart = new Chart();

        axes.chart = this.chart;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void draw( Graphics2D graphics, int x, int y, int width, int height )
    {
        // LogUtils.printDebug( "chart: w: " + width + ", h: " + height );

        // ---------------------------------------------------------------------
        // Clear
        // ---------------------------------------------------------------------
        graphics.setColor( Color.white );
        graphics.fillRect( 0, 0, width, height );

        int w = width - 40;
        int h = height - 40;

        // ---------------------------------------------------------------------
        // Draw plot.
        // ---------------------------------------------------------------------
        plot.draw( graphics, 20, 20, w, h );

        // ---------------------------------------------------------------------
        // Draw chart elements.
        // ---------------------------------------------------------------------
        axes.draw( graphics, 20, 20, w, h );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Dimension calculateSize()
    {
        return null;
    }
}
