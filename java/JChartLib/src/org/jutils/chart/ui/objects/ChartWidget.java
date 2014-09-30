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
    public final TextWidget topBottom;
    /**  */
    public final TextWidget title;
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
        this.chart = new Chart();
        this.topBottom = new TextWidget( chart.topBottomLabel );
        this.title = new TextWidget( chart.title );
        this.plot = new PlotWidget();
        this.axes = new AxesWidget();

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

        int titleHeight = title.calculateSize().height;

        int w = width;
        int h = height;

        Dimension d;

        // ---------------------------------------------------------------------
        // Draw top/bottom.
        // ---------------------------------------------------------------------
        if( chart.topBottomLabel.visible )
        {
            d = topBottom.calculateSize();

            topBottom.draw( graphics, 0, 0, w, d.height );
            topBottom.draw( graphics, 0, h - d.height, w, d.height );

            y += d.height;
            h -= 2 * d.height;
        }

        y += 10;
        h -= 20;

        // ---------------------------------------------------------------------
        // Draw title.
        // ---------------------------------------------------------------------
        title.draw( graphics, x, y, w, titleHeight );

        x += 20;
        y += 10 + titleHeight;
        w -= 40;
        h -= ( 20 + titleHeight );

        d = axes.calculateSize();

        // ---------------------------------------------------------------------
        // Draw axes.
        // ---------------------------------------------------------------------
        axes.draw( graphics, x, y, w, h );

        // ---------------------------------------------------------------------
        // Draw plot.
        // ---------------------------------------------------------------------
        plot.draw( graphics, x, y, w, h );
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
