package org.jutils.chart;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.jutils.chart.data.*;
import org.jutils.chart.model.ISeriesData;

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

    public static ISeriesData createLineSeries( int count, double slope,
        double offset, double min, double max )
    {
        List<XYPoint> points = new ArrayList<>();

        double m = ( max - min ) / count;

        for( int i = 0; i < count; i++ )
        {
            XYPoint pt = new XYPoint();

            pt.x = m * i + min;
            pt.y = slope * pt.x + offset;

            points.add( pt );
        }

        return new DefaultSeries( points );
    }

    public static ISeriesData createSinSeries( int count, double amplitude,
        double frequency, double phase, double min, double max )
    {
        List<XYPoint> points = new ArrayList<>();

        double m = ( max - min ) / count;

        for( int i = 0; i < count; i++ )
        {
            XYPoint pt = new XYPoint();

            pt.x = m * i + min;
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

    public static int findNearest( ISeriesData series, double x )
    {
        int lo = 0;
        int hi = series.getCount() - 1;
        int value = -1;

        if( series.getX( hi ) < x )
        {
            value = hi;
        }
        else if( x < series.getX( 0 ) )
        {
            value = 0;
        }
        else
        {
            while( value < 0 && lo <= hi )
            {
                // Key is in a[lo..hi] or not present.
                int mid = lo + ( hi - lo ) / 2;
                double x1 = series.getX( mid );
                double x2 = series.getX( mid + 1 );

                if( x < x1 )
                {
                    hi = mid - 1;
                }
                else if( x2 < x )
                {
                    lo = mid + 1;
                }
                else
                {
                    value = mid;
                }
            }
        }

        if( value < ( series.getCount() - 1 ) &&
            ( x - series.getX( value ) ) > ( series.getX( value + 1 ) - x ) )
        {
            value++;
        }

        return value;
    }
}
