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
    public final LegendWidget legend;
    /**  */
    final Chart chart;

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
        this.axes = new AxesWidget( context, chart );
        this.legend = new LegendWidget( this );
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
     * Draws the chart in the following order:<ol> <li>Top/Bottom Labels</li>
     * <li>Title</li> <li>Subtitle</li> <li>Legend</li> <li>Axes</li>
     * <li>Plot</li> </ol>
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

        Point wLoc = new Point( location.x, location.y );
        Dimension wSize = new Dimension( size );

        drawTopBottom( graphics, wLoc, wSize );

        drawTitle( graphics, wLoc, wSize );

        drawSubtitle( graphics, wLoc, wSize );

        // ---------------------------------------------------------------------
        //
        // ---------------------------------------------------------------------
        wLoc.x += 20;
        wSize.width -= 40;

        wLoc.y += 10;
        wSize.height -= 20;

        drawLegend( graphics, wLoc, wSize );

        // graphics.setStroke( new BasicStroke( 1 ) );
        // graphics.setColor( Color.red );
        // graphics.drawRect( wLoc.x, wLoc.y, wSize.width - 1, wSize.height - 1
        // );

        // ---------------------------------------------------------------------
        // Draw axes.
        // ---------------------------------------------------------------------
        axes.draw( graphics, wLoc, wSize );

        // ---------------------------------------------------------------------
        // Draw plot.
        // ---------------------------------------------------------------------
        wLoc = new Point( context.x, context.y );
        wSize = new Dimension( context.width, context.height );
        plot.draw( graphics, wLoc, wSize );
    }

    /***************************************************************************
     * @param graphics
     * @param wLoc
     * @param wSize
     **************************************************************************/
    private void drawSubtitle( Graphics2D graphics, Point wLoc, Dimension wSize )
    {
        if( chart.subtitle.visible )
        {
            int titleHeight = subtitle.calculateSize( wSize ).height;

            wLoc.y += 4;

            subtitle.draw( graphics, wLoc, wSize );

            wLoc.y += titleHeight;

            titleHeight += 4;

            wSize.height -= titleHeight;
        }
    }

    /***************************************************************************
     * @param graphics
     * @param wLoc
     * @param wSize
     **************************************************************************/
    private void drawTopBottom( Graphics2D graphics, Point wLoc, Dimension wSize )
    {
        if( chart.topBottomLabel.visible )
        {
            Dimension size = topBottom.calculateSize( wSize );
            Point loc = new Point( wLoc );

            size.width = wSize.width;

            loc.y += 4;
            topBottom.draw( graphics, loc, size );

            loc.y = wSize.height - size.height - 4;
            topBottom.draw( graphics, loc, size );

            wLoc.y += size.height + 4;
            wSize.height -= ( 2 * size.height + 8 );
        }
    }

    /***************************************************************************
     * @param graphics
     * @param wLoc
     * @param wSize
     **************************************************************************/
    private void drawTitle( Graphics2D graphics, Point wLoc, Dimension wSize )
    {
        if( chart.title.visible )
        {
            int titleHeight = title.calculateSize( wSize ).height;

            wLoc.y += 10;

            title.draw( graphics, wLoc, wSize );

            wLoc.y += titleHeight;

            titleHeight += 10;

            wSize.height -= titleHeight;
        }
    }

    /***************************************************************************
     * @param graphics
     * @param wLoc
     * @param wSize
     **************************************************************************/
    private void drawLegend( Graphics2D graphics, Point wLoc, Dimension wSize )
    {
        if( chart.legend.visible )
        {
            Dimension size = legend.calculateSize( wSize );
            Point loc = new Point( wLoc );

            switch( chart.legend.side )
            {
                case TOP:
                    size.width = wSize.width;
                    break;

                case LEFT:
                    size.height = wSize.height;
                    break;

                case BOTTOM:
                    loc.y = wLoc.y + wSize.height - size.height;
                    size.width = wSize.width;
                    break;

                case RIGHT:
                    loc.x = wLoc.x + wSize.width - size.width;
                    size.height = wSize.height;
                    break;
            }

            legend.draw( graphics, loc, new Dimension( size ) );

            switch( chart.legend.side )
            {
                case TOP:
                    wLoc.y += size.height + 8;
                    wSize.height -= ( size.height + 8 );
                    break;

                case LEFT:
                    wLoc.x += size.width + 8;
                    wSize.width -= ( size.width + 8 );
                    break;

                case BOTTOM:
                    wSize.height -= ( size.height + 8 );
                    break;

                case RIGHT:
                    wSize.width -= ( size.width + 8 );
                    break;
            }
        }
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
    public void setAutoBounds()
    {
        context.setAutoBounds( chart );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void calculateAutoBounds()
    {
        context.calculateAutoBounds( chart );
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
