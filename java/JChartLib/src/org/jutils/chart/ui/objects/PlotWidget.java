package org.jutils.chart.ui.objects;

import java.awt.Dimension;
import java.awt.Graphics2D;
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
                if( s.trackPoint && s.series.highlight.visible )
                {
                    s.highlight.draw( g2d, 0, 0, width, height );
                }
            }

            highlightLayer.repaint = false;

            selection.draw( g2d, context.x, context.y, context.width,
                context.height );
        }
        highlightLayer.paint( graphics, x, y );
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
