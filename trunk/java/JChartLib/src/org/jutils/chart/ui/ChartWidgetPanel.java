package org.jutils.chart.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChartWidgetPanel extends JComponent
{
    /**  */
    private IChartWidget object;
    /**  */
    private final Object lock;

    /***************************************************************************
     * 
     **************************************************************************/
    public ChartWidgetPanel()
    {
        this( null );
    }

    /***************************************************************************
     * @param object
     **************************************************************************/
    public ChartWidgetPanel( IChartWidget object )
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
