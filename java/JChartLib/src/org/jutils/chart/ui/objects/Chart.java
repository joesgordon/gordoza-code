package org.jutils.chart.ui.objects;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import org.jutils.chart.ChartContext;
import org.jutils.chart.ui.IJava2dObject;
import org.jutils.chart.ui.Layer2d;

public class Chart implements IJava2dObject
{
    private Layer2d axesLayer;
    private Layer2d seriesLayer;

    public final List<Series> serieses;

    public final ChartContext context;

    public Chart()
    {
        this.serieses = new ArrayList<Series>();

        this.axesLayer = new Layer2d();
        this.seriesLayer = new Layer2d();

        this.context = new ChartContext();
    }

    @Override
    public void paint( Graphics2D graphics, int width, int height )
    {
        Graphics2D g2d;

        g2d = axesLayer.setSize( width, height );
        graphics.drawImage( axesLayer.getImage(), 0, 0, null );

        g2d = seriesLayer.setSize( width, height );

        for( Series s : serieses )
        {
            s.context = context;
            s.paint( g2d, width, height );
        }

        graphics.drawImage( seriesLayer.getImage(), 0, 0, null );
    }
}
