package org.jutils.chart.series;

import org.jutils.chart.ISeries;

public class LineSeries implements ISeries
{

    private final int count;
    private final double slope;
    private final double offset;
    private final double min;
    private final double max;

    public LineSeries( int count, double slope, double offset, double min,
        double max )
    {
        this.count = count;
        this.slope = slope;
        this.offset = offset;
        this.min = min;
        this.max = max;
    }

    @Override
    public int getCount()
    {
        return count;
    }

    @Override
    public double getX( int index )
    {
        return ( max - min ) * index / count + min;
    }

    @Override
    public double getY( int index )
    {
        return getX( index );
    }
}
