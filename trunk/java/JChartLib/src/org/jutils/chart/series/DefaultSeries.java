package org.jutils.chart.series;

import java.util.ArrayList;
import java.util.List;

import org.jutils.chart.ISeries;

public class DefaultSeries implements ISeries
{
    public final List<XYPoint> points;

    public DefaultSeries()
    {
        points = new ArrayList<XYPoint>();
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
}
