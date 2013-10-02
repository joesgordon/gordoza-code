package org.jutils.chart;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.jutils.chart.data.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class ChartUtils
{
    /***************************************************************************
     * 
     **************************************************************************/
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
        p.x = ( int )( ( x - context.xMin ) / context.getXRange() * chartWidth );
        p.y = ( int )( chartHeight - ( y - context.yMin ) /
            context.getYRange() * chartHeight );
    }

    public static double coordsToValueX( int x, int chartWidth,
        ChartContext context )
    {
        return x * context.getXRange() / chartWidth + context.xMin;
    }

    public static ISeries createLineSeries( int count, double slope,
        double offset, double min, double max )
    {
        List<XYPoint> points = new ArrayList<>();

        for( int i = 0; i < count; i++ )
        {
            XYPoint pt = new XYPoint();

            pt.x = ( max - min + 1 ) * i / count + min;
            pt.y = slope * pt.x + offset;

            points.add( pt );
        }

        return new DefaultSeries( points );
    }

    public static ISeries createSinSeries( int count, double amplitude,
        double frequency, double phase, double min, double max )
    {
        List<XYPoint> points = new ArrayList<>();

        for( int i = 0; i < count; i++ )
        {
            XYPoint pt = new XYPoint();

            pt.x = ( max - min + 1 ) * i / count + min;
            pt.y = amplitude * Math.sin( frequency * ( pt.x + phase ) );

            points.add( pt );
        }

        return new DefaultSeries( points );
    }

    public static double distance( Point lastlp, Point p )
    {
        return Math.sqrt( Math.pow( lastlp.x - p.x, 2 ) +
            Math.pow( lastlp.y - p.y, 2 ) );
    }
}
