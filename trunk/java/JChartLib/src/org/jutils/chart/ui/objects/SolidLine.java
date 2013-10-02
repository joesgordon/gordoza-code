package org.jutils.chart.ui.objects;

import java.awt.*;

public class SolidLine implements ILine
{
    public Color color;
    public int size;
    public Point p1;
    public Point p2;

    private BasicStroke solidStroke;

    public SolidLine()
    {
        this.color = new Color( 0x0099CC );
        this.setSize( 4 );
    }

    @Override
    public void paint( Graphics2D graphics, int width, int height )
    {
        graphics.setColor( color );

        graphics.setStroke( solidStroke );

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
        this.size = size;
        this.solidStroke = new BasicStroke( size, BasicStroke.CAP_ROUND,
            BasicStroke.JOIN_ROUND );
    }

    @Override
    public double getSize()
    {
        return size;
    }
}
