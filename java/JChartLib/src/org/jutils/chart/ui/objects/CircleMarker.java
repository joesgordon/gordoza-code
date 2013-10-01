package org.jutils.chart.ui.objects;

import java.awt.*;

public class CircleMarker implements IMarker
{
    public Color color;
    public Color borderColor;
    public int radius;
    public int x;
    public int y;
    public boolean hasBorder;

    public CircleMarker()
    {
        color = new Color( 0x0066CC );
        borderColor = new Color( 0xCC0000 );

        radius = 6;
        x = 5;
        y = 5;

        hasBorder = true;
    }

    @Override
    public void paint( Graphics2D g, int width, int height )
    {
        g.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON );

        if( hasBorder )
        {
            g.setColor( borderColor );

            g.fillOval( x - 3, y - 3, radius + 6, radius + 6 );
        }

        g.setColor( color );

        g.fillOval( x, y, radius, radius );
    }

    @Override
    public void setBorderVisible( boolean visible )
    {
        hasBorder = visible;
    }

    @Override
    public void setLocation( Point p )
    {
        this.x = p.x;
        this.y = p.y;
    }

    @Override
    public void setColor( Color color )
    {
        this.color = color;
    }
}
