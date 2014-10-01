package org.jutils.chart.ui.objects;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import org.jutils.chart.data.ChartContext;
import org.jutils.chart.data.XYPoint;
import org.jutils.chart.ui.IChartWidget;
import org.jutils.chart.ui.Layer2d;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PlotWidget implements IChartWidget
{
    /**  */
    public final Layer2d seriesLayer;
    /**  */
    public final Layer2d highlightLayer;

    /**  */
    public final List<SeriesWidget> serieses;

    /**  */
    public final ChartContext context;

    /***************************************************************************
     * @param context
     **************************************************************************/
    public PlotWidget( ChartContext context )
    {
        this.context = context;
        this.serieses = new ArrayList<>();
        this.seriesLayer = new Layer2d();
        this.highlightLayer = new Layer2d();
        this.highlightLayer.repaint = false;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void draw( Graphics2D graphics, int x, int y, int width, int height )
    {
        Graphics2D g2d;

        // ---------------------------------------------------------------------
        // Draw series layer.
        // ---------------------------------------------------------------------
        g2d = seriesLayer.setSize( width, height );
        if( seriesLayer.repaint )
        {
            seriesLayer.clear();

            for( SeriesWidget s : serieses )
            {
                s.context = context;
                s.draw( g2d, x, y, width, height );
            }

            seriesLayer.repaint = false;
        }
        seriesLayer.paint( graphics, x, y );

        // ---------------------------------------------------------------------
        // Draw highlight layer.
        // ---------------------------------------------------------------------
        g2d = highlightLayer.setSize( width, height );
        if( highlightLayer.repaint )
        {
            highlightLayer.clear();

            for( SeriesWidget s : serieses )
            {
                s.highlight.draw( g2d, 0, 0, width, height );
            }

            highlightLayer.repaint = false;
        }
        highlightLayer.paint( graphics, x, y );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void calculateRanges( ChartContext context )
    {
        XYPoint min = new XYPoint( -5, 5 );
        XYPoint max = new XYPoint( -5, 5 );

        if( !serieses.isEmpty() )
        {
            min = new XYPoint( serieses.get( 0 ).series.data.getMin() );
            max = new XYPoint( serieses.get( 0 ).series.data.getMax() );
        }

        for( SeriesWidget s : serieses )
        {
            XYPoint.min( min, s.series.data.getMin(), min );
            XYPoint.max( max, s.series.data.getMax(), max );
        }

        context.xMin = min.x;
        context.yMin = min.y;

        context.xMax = max.x;
        context.yMax = max.y;

        context.xMin -= ( context.xMax - context.xMin ) * .03;
        context.xMax += ( context.xMax - context.xMin ) * .03;

        context.yMin -= ( context.yMax - context.yMin ) * .03;
        context.yMax += ( context.yMax - context.yMin ) * .03;
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
