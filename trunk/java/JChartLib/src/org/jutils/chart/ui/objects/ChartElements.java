package org.jutils.chart.ui.objects;

import java.awt.*;

import org.jutils.chart.ui.IChadget;
import org.jutils.chart.ui.Layer2d;

public class ChartElements implements IChadget
{
    public final Layer2d axesLayer;

    private BasicStroke solidStroke;

    public ChartElements()
    {
        this.axesLayer = new Layer2d();
        this.solidStroke = new BasicStroke( 4, BasicStroke.CAP_ROUND,
            BasicStroke.JOIN_ROUND );
    }

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
            axesLayer.clear();

            g2d.setColor( Color.black );

            g2d.setStroke( solidStroke );

            g2d.drawRect( 20, 20, width - 40, height - 40 );

            axesLayer.repaint = false;
        }
        axesLayer.paint( graphics );
    }
}
