package org.jutils.chart.ui.objects;

import java.awt.*;

import org.jutils.chart.data.ChartContext;
import org.jutils.chart.ui.IChadget;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Chart implements IChadget
{
    public final Plot plot;
    public final ChartElements elements;

    public final ChartContext context;

    public Chart()
    {
        this.plot = new Plot();
        this.elements = new ChartElements();

        this.context = new ChartContext();
    }

    @Override
    public void paint( Graphics2D graphics, int width, int height )
    {
        graphics.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON );

        graphics.setColor( Color.white );

        graphics.fillRect( 0, 0, width, height );

        context.width = width - 40;
        context.height = height - 40;

        // ---------------------------------------------------------------------
        // Draw plot.
        // ---------------------------------------------------------------------
        plot.x = 20;
        plot.y = 20;
        plot.context = context;
        plot.paint( graphics, context.width, context.height );

        // ---------------------------------------------------------------------
        // Draw chart elements.
        // ---------------------------------------------------------------------
        elements.paint( graphics, width, height );
    }
}
