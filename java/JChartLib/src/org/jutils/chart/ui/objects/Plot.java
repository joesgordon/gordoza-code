package org.jutils.chart.ui.objects;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import org.jutils.chart.data.ChartContext;
import org.jutils.chart.ui.IChadget;
import org.jutils.chart.ui.Layer2d;

public class Plot implements IChadget
{
    public final Layer2d seriesLayer;
    public final Layer2d highlightLayer;

    public final List<Series> serieses;

    public final ChartContext context;
    public int x;
    public int y;

    public Plot()
    {
        this.context = new ChartContext();
        this.serieses = new ArrayList<>();
        this.seriesLayer = new Layer2d();
        this.highlightLayer = new Layer2d();
        this.highlightLayer.repaint = false;
    }

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

            for( Series s : serieses )
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

            for( Series s : serieses )
            {
                s.highlightMarker.paint( g2d, width, height );
            }

            highlightLayer.repaint = false;
        }
        highlightLayer.paint( graphics, x, y );
    }
}