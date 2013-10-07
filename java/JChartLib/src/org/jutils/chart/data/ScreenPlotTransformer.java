package org.jutils.chart.data;

import java.awt.Point;

public class ScreenPlotTransformer
{
    private final ChartContext context;
    private final int chartHeight;

    private final double xScale;
    private final double yScale;

    public ScreenPlotTransformer( ChartContext context )
    {
        this.context = context;
        this.chartHeight = context.height;

        this.xScale = context.width / context.getXRange();
        this.yScale = context.height / context.getYRange();
    }

    public void fromChart( XYPoint xy, Point p )
    {
        p.x = ( int )( ( xy.x - context.xMin ) * xScale );
        p.y = ( int )( chartHeight - ( xy.y - context.yMin ) * yScale );
    }

    public void fromScreen( Point p, XYPoint xy )
    {
        xy.x = p.x / xScale + context.xMin;
        xy.y = -1 * p.y / yScale + context.yMin;
    }
}
