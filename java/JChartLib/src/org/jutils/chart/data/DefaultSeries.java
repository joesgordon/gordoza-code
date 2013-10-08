package org.jutils.chart.data;

import java.util.*;

public class DefaultSeries implements ISeries
{
    private final List<XYPoint> points;
    private final XYPoint min;
    private final XYPoint max;

    public DefaultSeries( List<XYPoint> points )
    {
        this.points = new ArrayList<XYPoint>( points );
        this.min = new XYPoint( points.get( 0 ) );
        this.max = new XYPoint( points.get( 0 ) );

        for( XYPoint p : points )
        {
            if( p.x < min.x )
            {
                min.x = p.x;
            }

            if( p.y < min.y )
            {
                min.y = p.y;
            }

            if( p.x > max.x )
            {
                max.x = p.x;
            }

            if( p.y > max.y )
            {
                max.y = p.y;
            }
        }
    }

    @Override
    public int getCount()
    {
        return points.size();
    }

    @Override
    public double getX( int index )
    {
        return points.get( index ).x;
    }

    @Override
    public double getY( int index )
    {
        return points.get( index ).y;
    }

    @Override
    public Iterator<XYPoint> iterator()
    {
        return points.iterator();
    }

    @Override
    public XYPoint get( int index )
    {
        return points.get( index );
    }

    @Override
    public XYPoint getMin()
    {
        return min;
    }

    @Override
    public XYPoint getMax()
    {
        return max;
    }
}
