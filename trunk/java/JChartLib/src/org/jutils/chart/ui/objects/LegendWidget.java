package org.jutils.chart.ui.objects;

import java.awt.*;

import org.jutils.chart.model.Chart;
import org.jutils.chart.ui.IChartWidget;

/*******************************************************************************
 * 
 ******************************************************************************/
public class LegendWidget implements IChartWidget
{
    private final Chart chart;

    public LegendWidget( Chart chart )
    {
        this.chart = chart;
    }

    @Override
    public Dimension calculateSize( Dimension canvasSize )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void draw( Graphics2D graphics, Point location, Dimension size )
    {
        // TODO Auto-generated method stub

    }
}
