package org.jutils.chart.ui.objects;

import java.awt.*;

import org.jutils.chart.data.Bounds;
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
    public final TextWidget subtitle;
    /**  */
    public final PlotWidget plot;
    /**  */
    public final AxesWidget axes;
    /**  */
    private final Chart chart;

    /***************************************************************************
     * @param chart
     **************************************************************************/
    public ChartWidget( Chart chart )
    {
        this.chart = chart;

        this.context = new ChartContext();
        this.topBottom = new TextWidget( chart.topBottomLabel );
        this.title = new TextWidget( chart.title );
        this.subtitle = new TextWidget( chart.subtitle );
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

        int w = width;
        int h = height;

        Dimension d;

        // ---------------------------------------------------------------------
        // Draw top/bottom.
        // ---------------------------------------------------------------------
        if( chart.topBottomLabel.visible )
        {
            d = topBottom.calculateSize();

            y += 4;

            topBottom.draw( graphics, x, y, w, d.height );
            topBottom.draw( graphics, x, y + h - d.height - 4, w, d.height );

            y += d.height;
            h -= ( 2 * d.height + 4 );
        }

        // ---------------------------------------------------------------------
        // Draw title.
        // ---------------------------------------------------------------------
        if( chart.title.visible )
        {
            int titleHeight = title.calculateSize().height;

            y += 10;

            title.draw( graphics, x, y, w, titleHeight );

            y += titleHeight;

            titleHeight += 10;

            h -= titleHeight;
        }

        // ---------------------------------------------------------------------
        // Draw subtitle.
        // ---------------------------------------------------------------------
        if( chart.subtitle.visible )
        {
            int titleHeight = subtitle.calculateSize().height;

            y += 4;

            subtitle.draw( graphics, x, y, w, titleHeight );

            y += titleHeight;

            titleHeight += 4;

            h -= titleHeight;
        }

        x += 20;
        w -= 40;

        y += 10;
        h -= 20;

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

    /***************************************************************************
     * @param visible
     **************************************************************************/
    public void setTrackingVisible( boolean visible )
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
        context.setAutoBounds( chart );
    }

    /***************************************************************************
     * @param b
     **************************************************************************/
    public void setBounds( Bounds b )
    {
        context.setBounds( b );

        axes.axesLayer.repaint = true;
    }
}
