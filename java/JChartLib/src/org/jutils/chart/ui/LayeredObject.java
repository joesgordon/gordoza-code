package org.jutils.chart.ui;

import java.awt.*;

//TODO Use or lose!

/*******************************************************************************
 * 
 ******************************************************************************/
public class LayeredObject implements IChartWidget
{
    /**  */
    private final Layer2d layer;
    /**  */
    private final IChartWidget object;

    /***************************************************************************
     * @param obj
     **************************************************************************/
    public LayeredObject( IChartWidget obj )
    {
        this.object = obj;

        this.layer = new Layer2d();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Dimension calculateSize( Dimension canvasSize )
    {
        return object.calculateSize( canvasSize );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void draw( Graphics2D graphics, Point location, Dimension size )
    {
        Graphics2D g2d;

        g2d = layer.setSize( size.width, size.height );

        if( layer.repaint )
        {
            object.draw( g2d, location, size );
        }

        layer.paint( graphics, 0, 0 );
    }
}
