package org.jutils.chart.ui.objects;

import java.awt.*;

import org.jutils.chart.ui.IChadget;
import org.jutils.chart.ui.Layer2d;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChartElements implements IChadget
{
    /**  */
    public final Layer2d axesLayer;

    /**  */
    private BasicStroke solidStroke;
    /**  */
    private int width;
    /**  */
    private int inset;

    /***************************************************************************
     * 
     **************************************************************************/
    public ChartElements()
    {
        this.width = 2;
        this.inset = 20;
        this.axesLayer = new Layer2d();
        this.solidStroke = new BasicStroke( width, BasicStroke.CAP_ROUND,
            BasicStroke.JOIN_ROUND );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void paint( Graphics2D graphics, int width, int height )
    {
        Graphics2D g2d;

        // ---------------------------------------------------------------------
        // Draw axes layer.
        // ---------------------------------------------------------------------
        g2d = axesLayer.setSize( width, height );
        if( axesLayer.repaint )
        {
            int x = inset - this.width / 2;
            int w = width - 2 * inset + this.width;
            int h = height - 2 * inset + this.width;

            axesLayer.clear();

            g2d.setColor( Color.black );

            g2d.setStroke( solidStroke );

            g2d.drawRect( x, x, w, h );

            axesLayer.repaint = false;
        }
        axesLayer.paint( graphics );
    }
}
