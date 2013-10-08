package org.jutils.chart.data;

public interface ISeries extends Iterable<XYPoint>
{
    public int getCount();

    public double getX( int index );

    public XYPoint getMin();

    public XYPoint getMax();

    public double getY( int index );

    public XYPoint get( int index );
}
