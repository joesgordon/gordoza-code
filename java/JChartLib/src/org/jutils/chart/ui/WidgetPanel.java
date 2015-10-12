package org.jutils.chart.ui;

import java.awt.*;

import javax.swing.JComponent;

/*******************************************************************************
 * 
 ******************************************************************************/
public class WidgetPanel extends JComponent
{
    /**  */
    private IChartWidget object;
    /**  */
    private final Object lock;
    /**  */
    private final Layer2d layer;

    /***************************************************************************
     * 
     **************************************************************************/
    public WidgetPanel()
    {
        this( null );
    }

    /***************************************************************************
     * @param object
     **************************************************************************/
    public WidgetPanel( IChartWidget object )
    {
        this.lock = new Object();
        this.object = object;
        this.layer = new Layer2d();
    }

    /***************************************************************************
     * @param obj
     **************************************************************************/
    public void setObject( IChartWidget obj )
    {
        this.object = obj;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void paintComponent( Graphics g )
    {
        super.paintComponent( g );

        IChartWidget obj = null;

        synchronized( lock )
        {
            obj = object;
        }

        if( obj == null )
        {
            return;
        }

        Dimension size = new Dimension( getWidth(), getHeight() );
        Dimension min = getMinimumSize();

        size.width = Math.max( size.width, min.width );
        size.height = Math.max( size.height, min.height );

        layer.setSize( size );

        obj.calculateSize( size );
        obj.draw( layer.getGraphics(), new Point(), size );

        layer.paint( ( Graphics2D )g );

        // obj.draw( ( Graphics2D )g, new Point(), size );
    }
}
