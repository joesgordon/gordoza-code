package org.jutils.chart.ui.objects;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import org.jutils.chart.data.ChartContext;
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
    public final SelectionWidget selection;

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

        this.seriesLayer = new Layer2d();
        this.highlightLayer = new Layer2d();
        this.selection = new SelectionWidget( context );
        this.serieses = new ArrayList<>();

        highlightLayer.repaint = false;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void draw( Graphics2D graphics, Point location, Dimension size )
    {
        Graphics2D g2d;

        int x = location.x;
        int y = location.y;
        int width = size.width;
        int height = size.height;

        Point p;
        Dimension d;

        if( width < 1 || height < 1 )
        {
            return;
        }

        // ---------------------------------------------------------------------
        // Draw series layer.
        // ---------------------------------------------------------------------
        g2d = seriesLayer.setSize( width, height );
        if( seriesLayer.repaint )
        {
            seriesLayer.clear();

            for( SeriesWidget s : serieses )
            {
                s.draw( g2d, location, size );
            }

            seriesLayer.repaint = false;
        }
        seriesLayer.paint( graphics, x, y );

        // ---------------------------------------------------------------------
        // Draw highlight layer.
        // ---------------------------------------------------------------------
        p = new Point( context.x, context.y );
        d = new Dimension( context.width, context.height );
        g2d = highlightLayer.setSize( width, height );
        if( highlightLayer.repaint )
        {
            highlightLayer.clear();

            for( SeriesWidget s : serieses )
            {
                if( s.series.visible && s.trackPoint &&
                    s.series.highlight.visible )
                {
                    s.highlight.draw( g2d, location, size );
                }
            }

            highlightLayer.repaint = false;

            selection.draw( g2d, p, d );
        }
        highlightLayer.paint( graphics, x, y );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Dimension calculateSize( Dimension canvasSize )
    {
        return null;
    }
}
