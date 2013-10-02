package org.jutils.chart.data;

import java.awt.Point;

import org.jutils.chart.ChartContext;

public class ScreenPlotTransformer
{
    private final ChartContext context;
    private final int chartHeight;

    private final double xScale;
    private final double yScale;

    public ScreenPlotTransformer( ChartContext context, int chartWidth,
        int chartHeight )
    {
        this.context = context;
        this.chartHeight = chartHeight;

        this.xScale = chartWidth / context.getXRange();
        this.yScale = chartHeight / context.getYRange();
    }

    public void fromChart( XYPoint xy, Point p )
    {
        p.x = ( int )( ( xy.x - context.xMin ) * xScale );
        p.y = ( int )( chartHeight - ( xy.y - context.yMin ) * yScale );
    }

    public void fromScreen( Point p, XYPoint xy )
    {
        xy.x = p.x / xScale + context.xMin;
        xy.y = ( p.y - chartHeight ) / yScale + context.yMin;
    }
}
