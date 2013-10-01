package org.jutils.chart.ui.objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import org.jutils.chart.ChartContext;
import org.jutils.chart.ui.IJava2dObject;
import org.jutils.chart.ui.Layer2d;

public class Chart implements IJava2dObject
{
    public final Layer2d axesLayer;
    public final Layer2d seriesLayer;
    public final Layer2d highlightLayer;

    public final List<Series> serieses;

    public final ChartContext context;

    public Chart()
    {
        this.serieses = new ArrayList<Series>();

        this.axesLayer = new Layer2d();
        this.seriesLayer = new Layer2d();
        this.highlightLayer = new Layer2d();
        this.highlightLayer.repaint = false;

        this.context = new ChartContext();
    }

    @Override
    public void paint( Graphics2D graphics, int width, int height )
    {
        Graphics2D g2d;

        graphics.setColor( Color.white );

        graphics.fillRect( 0, 0, width, height );

        // ---------------------------------------------------------------------
        // Draw axes layer.
        // ---------------------------------------------------------------------
        g2d = axesLayer.setSize( width, height );
        if( axesLayer.repaint )
        {
            axesLayer.clear();

            axesLayer.repaint = false;
        }
        axesLayer.paint( graphics );

        // ---------------------------------------------------------------------
        // Draw series layer.
        // ---------------------------------------------------------------------
        g2d = seriesLayer.setSize( width - 0, height - 0 );
        if( seriesLayer.repaint )
        {
            seriesLayer.clear();

            for( Series s : serieses )
            {
                s.context = context;
                s.paint( g2d, width - 0, height - 0 );
            }

            seriesLayer.repaint = false;
        }
        seriesLayer.paint( graphics, 0, 0 );

        // ---------------------------------------------------------------------
        // Draw highlight layer.
        // ---------------------------------------------------------------------
        g2d = highlightLayer.setSize( width, height );
        if( highlightLayer.repaint )
        {
            highlightLayer.clear();

            for( Series s : serieses )
            {
                s.highlightMarker.paint( g2d, width, height );
            }

            highlightLayer.repaint = false;
        }
        highlightLayer.paint( graphics );
    }
}
