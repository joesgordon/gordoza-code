package org.jutils.chart.ui.objects;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import org.jutils.chart.data.ChartContext;
import org.jutils.chart.data.XYPoint;
import org.jutils.chart.ui.IChadget;
import org.jutils.chart.ui.Layer2d;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PlotWidget implements IChadget
{
    /**  */
    public final Layer2d seriesLayer;
    /**  */
    public final Layer2d highlightLayer;

    /**  */
    public final List<SeriesWidget> serieses;

    /**  */
    public final ChartContext context;

    /**  */
    public int x;
    /**  */
    public int y;

    /***************************************************************************
     * 
     **************************************************************************/
    public PlotWidget()
    {
        this.context = new ChartContext();
        this.serieses = new ArrayList<>();
        this.seriesLayer = new Layer2d();
        this.highlightLayer = new Layer2d();
        this.highlightLayer.repaint = false;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void paint( Graphics2D graphics, int width, int height )
    {
        Graphics2D g2d;

        context.width = width;
        context.height = height;

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
                s.paint( g2d, width, height );
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
                s.highlight.paint( g2d, width, height );
            }

            highlightLayer.repaint = false;
        }
        highlightLayer.paint( graphics, x, y );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void calculateRanges()
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

        context.yMin -= ( context.yMax - context.yMin ) * .03;
        context.yMax += ( context.yMax - context.yMin ) * .03;
    }
}
