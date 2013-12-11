package org.jutils.chart.ui.objects;

import java.awt.Graphics2D;

import org.jutils.chart.ui.IChadget;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Axis implements IChadget
{
    public final AxisTicks ticks;

    public Axis()
    {
        this.ticks = new AxisTicks();
    }

    @Override
    public void paint( Graphics2D graphics, int width, int height )
    {
        // TODO Auto-generated method stub
    }
}
