package org.jutils.chart.ui.objects;

import java.awt.*;

import org.jutils.chart.data.ChartContext;
import org.jutils.chart.model.Chart;
import org.jutils.chart.ui.IChartWidget;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChartWidget implements IChartWidget
{
    /**  */
    public final ChartContext context;
    /**  */
    public final TextWidget topBottom;
    /**  */
    public final TextWidget title;
    /**  */
    public final PlotWidget plot;
    /**  */
    public final AxesWidget axes;
    /**  */
    private final Chart chart;

    /***************************************************************************
     * 
     **************************************************************************/
    public ChartWidget( Chart chart )
    {
        this.chart = chart;

        this.context = new ChartContext();
        this.topBottom = new TextWidget( chart.topBottomLabel );
        this.title = new TextWidget( chart.title );
        this.plot = new PlotWidget( context );
        this.axes = new AxesWidget( context );

        axes.chart = chart;
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
        graphics.fillRect( x, y, width, height );

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

            topBottom.draw( graphics, x, y, w, d.height );
            topBottom.draw( graphics, x, y + h - d.height, w, d.height );

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
        plot.draw( graphics, context.x, context.y, context.width,
            context.height );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Dimension calculateSize()
    {
        return null;
    }

    public void setVolatileVisible( boolean visible )
    {
        for( SeriesWidget s : plot.serieses )
        {
            s.trackPoint = visible;
        }

        plot.highlightLayer.repaint = true;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void calculateBounds()
    {
        context.calculate( chart );
    }
}
