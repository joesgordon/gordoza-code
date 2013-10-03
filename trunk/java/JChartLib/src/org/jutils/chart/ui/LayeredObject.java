package org.jutils.chart.ui;

import java.awt.Graphics2D;

//TODO Use or lose!

public class LayeredObject implements IChadget
{
    private final Layer2d layer;
    private final IChadget object;

    public LayeredObject( IChadget obj )
    {
        this.object = obj;

        this.layer = new Layer2d();
    }

    @Override
    public void paint( Graphics2D graphics, int width, int height )
    {
        Graphics2D g2d;

        g2d = layer.setSize( width, height );

        if( layer.repaint )
        {
            object.paint( g2d, width, height );
        }

        layer.paint( graphics, 0, 0 );
    }
}
