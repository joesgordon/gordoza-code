package org.jutils.chart.ui.objects;

import java.awt.*;

import org.jutils.chart.model.IMarker;

/*******************************************************************************
 * 
 ******************************************************************************/
public class CircleBorderMarker implements IMarker
{
    /**  */
    private Color color;
    /**  */
    private Color borderColor;
    /**  */
    private int radius;
    /**  */
    private int radiusHalf;
    /**  */
    private int x;
    /**  */
    private int y;

    /***************************************************************************
     * 
     **************************************************************************/
    public CircleBorderMarker()
    {
        color = new Color( 0x0066CC );
        borderColor = new Color( 0xCC0000 );

        setSize( 6 );
        x = 5;
        y = 5;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void draw( Graphics2D g, Point location, Dimension size )
    {
        {
            g.setColor( borderColor );

            g.fillOval( x - radiusHalf - 2, y - radiusHalf - 2, radius + 4,
                radius + 4 );
        }

        g.setColor( color );

        g.fillOval( x - radiusHalf, y - radiusHalf, radius, radius );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setLocation( Point p )
    {
        this.x = p.x;
        this.y = p.y;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setColor( Color color )
    {
        this.color = color;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void setBorderColor( Color color )
    {
        this.borderColor = color;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void setSize( int r )
    {
        this.radius = r;
        this.radiusHalf = r / 2;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Dimension calculateSize( Dimension canvasSize )
    {
        return new Dimension( radius, radius );
    }
}
