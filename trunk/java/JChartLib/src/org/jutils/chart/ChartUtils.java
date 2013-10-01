package org.jutils.chart;

import java.awt.Point;

import org.jutils.chart.series.DefaultSeries;
import org.jutils.chart.series.XYPoint;

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

    public static ISeries createLineSeries( int count, double slope,
        double offset, double min, double max )
    {
        DefaultSeries series = new DefaultSeries();

        for( int i = 0; i < count; i++ )
        {
            XYPoint pt = new XYPoint();

            pt.x = ( max - min ) * i / count + min;
            pt.y = slope * pt.x + offset;

            series.points.add( pt );
        }

        return series;
    }

    public static ISeries createSinSeries( int count, double scale,
        double phase, double min, double max )
    {
        DefaultSeries series = new DefaultSeries();

        for( int i = 0; i < count; i++ )
        {
            XYPoint pt = new XYPoint();

            pt.x = ( max - min ) * i / count + min;
            pt.y = scale * Math.sin( pt.x + phase );

            series.points.add( pt );
        }

        return series;
    }
}
