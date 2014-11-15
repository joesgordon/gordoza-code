package org.jutils.chart.ui.objects;

import java.awt.*;

import org.jutils.chart.model.IMarker;

/*******************************************************************************
 * 
 ******************************************************************************/
public class CircleMarker implements IMarker
{
    /**  */
    private Color color;
    /**  */
    private int diameter;
    /**  */
    private int radius;
    /**  */
    private int x;
    /**  */
    private int y;

    /***************************************************************************
     * 
     **************************************************************************/
    public CircleMarker()
    {
        this.x = 5;
        this.y = 5;
        this.setSize( 6 );
        this.setColor( new Color( 0x0066CC ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void draw( Graphics2D g, Point location, Dimension size )
    {
        g.setColor( color );

        g.fillOval( x - radius, y - radius, diameter, diameter );
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
    @Override
    public void setSize( int r )
    {
        this.diameter = r;
        this.radius = r / 2;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Dimension calculateSize( Dimension canvasSize )
    {
        return new Dimension( diameter, diameter );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int getSize()
    {
        return diameter;
    }
}
