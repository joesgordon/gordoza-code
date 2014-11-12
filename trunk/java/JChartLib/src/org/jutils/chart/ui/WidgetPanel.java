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

        Graphics2D g2d = ( Graphics2D )g;

        Dimension size = new Dimension( getWidth(), getHeight() );
        Dimension min = getMinimumSize();

        size.width = Math.max( size.width, min.width );
        size.height = Math.max( size.height, min.height );

        obj.draw( g2d, new Point(), size );
    }
}
