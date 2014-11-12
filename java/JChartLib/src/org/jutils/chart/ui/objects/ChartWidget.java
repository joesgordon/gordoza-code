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
    public Dimension calculateSize( Dimension canvasSize )
    {
        // TODO Auto-generated method stub
        return null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void draw( Graphics2D graphics, Point location, Dimension size )
    {
        // LogUtils.printDebug( "chart: w: " + width + ", h: " + height );

        // ---------------------------------------------------------------------
        // Clear
        // ---------------------------------------------------------------------
        graphics.setColor( Color.white );
        graphics.fillRect( location.x, location.y, size.width, size.height );

        Point p = new Point( location.x, location.y );
        Dimension d = new Dimension( size );

        // ---------------------------------------------------------------------
        // Draw top/bottom.
        // ---------------------------------------------------------------------
        if( chart.topBottomLabel.visible )
        {
            d = topBottom.calculateSize( size );

            p.y += 4;
            topBottom.draw( graphics, p, d );

            p.y += 4 + size.height - d.height;
            topBottom.draw( graphics, p, d );

            p.y += d.height;
            d.height -= ( 2 * d.height + 4 );
        }

        // ---------------------------------------------------------------------
        // Draw title.
        // ---------------------------------------------------------------------
        if( chart.title.visible )
        {
            int titleHeight = title.calculateSize( size ).height;

            p.y += 10;

            title.draw( graphics, p, d );

            p.y += titleHeight;

            titleHeight += 10;

            d.height -= titleHeight;
        }

        // ---------------------------------------------------------------------
        // Draw subtitle.
        // ---------------------------------------------------------------------
        if( chart.subtitle.visible )
        {
            int titleHeight = subtitle.calculateSize( size ).height;

            p.y += 4;

            subtitle.draw( graphics, p, d );

            p.y += titleHeight;

            titleHeight += 4;

            d.height -= titleHeight;
        }

        p.x += 20;
        d.width -= 40;

        p.y += 10;
        d.height -= 20;

        // ---------------------------------------------------------------------
        // Draw axes.
        // ---------------------------------------------------------------------
        axes.draw( graphics, p, d );

        // ---------------------------------------------------------------------
        // Draw plot.
        // ---------------------------------------------------------------------
        p = new Point( context.x, context.y );
        d = new Dimension( context.width, context.height );
        plot.draw( graphics, p, d );
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
