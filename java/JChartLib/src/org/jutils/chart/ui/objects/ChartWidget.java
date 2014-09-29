package org.jutils.chart.ui.objects;

import java.awt.*;

import org.jutils.chart.ui.IChadget;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChartWidget implements IChadget
{
    /**  */
    public final PlotWidget plot;
    /**  */
    public final ChartElements elements;

    /***************************************************************************
     * 
     **************************************************************************/
    public ChartWidget()
    {
        this.plot = new PlotWidget();
        this.elements = new ChartElements();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void paint( Graphics2D graphics, int width, int height )
    {
        graphics.setRenderingHint( RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON );

        graphics.setColor( Color.white );

        graphics.fillRect( 0, 0, width, height );

        // ---------------------------------------------------------------------
        // Draw plot.
        // ---------------------------------------------------------------------
        plot.x = 20;
        plot.y = 20;
        plot.paint( graphics, width - 40, height - 40 );

        // ---------------------------------------------------------------------
        // Draw chart elements.
        // ---------------------------------------------------------------------
        elements.paint( graphics, width, height );
    }
}
