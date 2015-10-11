package org.jutils.chart.ui.objects;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import org.jutils.chart.ui.IChartWidget;
import org.jutils.chart.ui.Layer2d;

/*******************************************************************************
 * 
 ******************************************************************************/
public class PlotsWidget implements IChartWidget
{
    /**  */
    private final Layer2d seriesLayer;
    /**  */
    public final Layer2d highlightLayer;
    /**  */
    public final SelectionWidget selection;

    /**  */
    public final List<PlotWidget> plots;

    /**  */
    public final PlotContext context;

    /***************************************************************************
     * @param context
     **************************************************************************/
    public PlotsWidget( PlotContext context )
    {
        this.context = context;

        this.seriesLayer = new Layer2d();
        this.highlightLayer = new Layer2d();
        this.selection = new SelectionWidget( context );
        this.plots = new ArrayList<>();

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

        Point pt;
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

            for( PlotWidget s : plots )
            {
                s.draw( g2d, location, size );
            }

            seriesLayer.repaint = false;
        }
        seriesLayer.paint( graphics, x, y );

        // ---------------------------------------------------------------------
        // Draw highlight layer.
        // ---------------------------------------------------------------------
        pt = new Point( context.x, context.y );
        d = new Dimension( context.width, context.height );
        g2d = highlightLayer.setSize( width, height );

        if( highlightLayer.repaint )
        {
            highlightLayer.clear();

            for( PlotWidget p : plots )
            {
                if( p.series.visible && p.trackPoint &&
                    p.highlightLocation != null )
                {
                    p.highlight.draw( g2d, p.highlightLocation );
                }
            }

            highlightLayer.repaint = false;

            selection.draw( g2d, pt, d );
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

    /***************************************************************************
     * 
     **************************************************************************/
    public void repaint()
    {
        seriesLayer.repaint = true;
        highlightLayer.repaint = true;
    }
}
