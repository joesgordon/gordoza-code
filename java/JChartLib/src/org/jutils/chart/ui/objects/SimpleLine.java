package org.jutils.chart.ui.objects;

import java.awt.*;

public class SimpleLine implements ILine
{
    public Color color;
    public Point p1;
    public Point p2;

    public SimpleLine()
    {
        this.color = new Color( 0x0066CC );
        this.setSize( 4 );
    }

    @Override
    public void paint( Graphics2D graphics, int width, int height )
    {
        graphics.setColor( color );

        graphics.drawLine( p1.x, p1.y, p2.x, p2.y );
    }

    @Override
    public void setPoints( Point p1, Point p2 )
    {
        this.p1 = p1;
        this.p2 = p2;
    }

    @Override
    public void setColor( Color color )
    {
        this.color = color;
    }

    @Override
    public void setSize( int size )
    {
    }

    @Override
    public double getSize()
    {
        return 1;
    }
}
