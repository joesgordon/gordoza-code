package org.jutils.chart.data;

import java.util.*;

public class DefaultSeries implements ISeries
{
    private final List<XYPoint> points;

    public DefaultSeries( List<XYPoint> points )
    {
        this.points = new ArrayList<XYPoint>( points );
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
}
