package org.jutils.chart;

import java.awt.Point;

public final class ChartUtils
{
    private ChartUtils()
    {
    }

    public static Point valueToChartCoords( double x, double y, int chartWidth,
        int chartHeight, ChartContext context )
    {
        Point p = new Point();

        p.x = ( int )( ( x - context.xMin ) / context.xRange * chartWidth );
        p.y = ( int )( chartHeight - ( y - context.yMin ) / context.yRange *
            chartHeight );

        return p;
    }
}
