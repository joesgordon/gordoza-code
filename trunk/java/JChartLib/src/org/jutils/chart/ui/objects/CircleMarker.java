package org.jutils.chart.ui.objects;

import java.awt.*;

public class CircleMarker implements IMarker
{
    public int radius;
    public int x;
    public int y;
    public boolean hasBorder;

    public CircleMarker()
    {
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
            g.setColor( new Color( 0xcc0000 ) );

            g.fillOval( x - 2, y - 2, radius + 4, radius + 4 );
        }

        g.setColor( new Color( 0x0066cc ) );

        g.fillOval( x, y, radius, radius );
    }

    @Override
    public void setX( int x )
    {
        this.x = x;
    }

    @Override
    public void setY( int y )
    {
        this.y = y;
    }

    @Override
    public void setBorderVisible( boolean visible )
    {
        hasBorder = visible;
    }
}
