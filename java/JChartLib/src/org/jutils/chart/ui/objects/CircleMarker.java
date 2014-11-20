package org.jutils.chart.ui.objects;

import java.awt.*;
import java.awt.geom.Ellipse2D;

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
    private final Ellipse2D.Float ellipse;
    /**  */
    private int x;
    /**  */
    private int y;

    /***************************************************************************
     * 
     **************************************************************************/
    public CircleMarker()
    {
        this.setColor( new Color( 0x0066CC ) );
        this.ellipse = new Ellipse2D.Float( 0, 0, 6, 6 );
        this.x = -5;
        this.y = -5;

        setSize( 6 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void draw( Graphics2D g, Point location, Dimension size )
    {
        g.setColor( color );

        ellipse.x = x - radius;
        ellipse.y = y - radius;

        g.fill( ellipse );

        // g.fillOval( x - radius, y - radius, diameter, diameter );
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
    public void setSize( int diameter )
    {
        this.ellipse.height = diameter;
        this.ellipse.width = diameter;
        this.diameter = diameter;
        this.radius = diameter / 2;
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
