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

        valueToChartCoords( x, y, chartWidth, chartHeight, context, p );

        return p;
    }

    public static void valueToChartCoords( double x, double y, int chartWidth,
        int chartHeight, ChartContext context, Point p )
    {
        p.x = ( int )( ( x - context.xMin ) / context.xRange * chartWidth );
        p.y = ( int )( chartHeight - ( y - context.yMin ) / context.yRange *
            chartHeight );
    }

    public static double coordsToValueX( int x, int chartWidth,
        ChartContext context )
    {
        return x * context.xRange / chartWidth + context.xMin;
    }
}
