package org.jutils.chart.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;

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

        obj.draw( g2d, 0, 0, getWidth(), getHeight() );
    }
}
