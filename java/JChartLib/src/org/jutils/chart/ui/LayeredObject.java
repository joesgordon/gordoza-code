package org.jutils.chart.ui;

import java.awt.Dimension;
import java.awt.Graphics2D;

//TODO Use or lose!

public class LayeredObject implements IChartWidget
{
    private final Layer2d layer;
    private final IChartWidget object;

    public LayeredObject( IChartWidget obj )
    {
        this.object = obj;

        this.layer = new Layer2d();
    }

    @Override
    public void draw( Graphics2D graphics, int x, int y, int width, int height )
    {
        Graphics2D g2d;

        g2d = layer.setSize( width, height );

        if( layer.repaint )
        {
            object.draw( g2d, x, y, width, height );
        }

        layer.paint( graphics, 0, 0 );
    }

    @Override
    public Dimension calculateSize()
    {
        return object.calculateSize();
    }
}
