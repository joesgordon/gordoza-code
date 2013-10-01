package org.jutils.chart.series;

import org.jutils.chart.ISeries;

public class SinSeries implements ISeries
{
    private final int count;
    private final double scale;
    private final double phase;
    private final double min;
    private final double max;

    public SinSeries( int count, double scale, double phase, double min,
        double max )
    {
        this.count = count;
        this.scale = scale;
        this.phase = phase;
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
        return scale * Math.sin( getX( index ) + phase );
    }

}
