package org.jutils.chart.data;

public class XYPoint
{
    public double x;
    public double y;

    public XYPoint()
    {
        this( 0, 0 );
    }

    public XYPoint( double x, double y )
    {
        this.x = x;
        this.y = y;
    }

    public XYPoint( XYPoint xy )
    {
        this.x = xy.x;
        this.y = xy.y;
    }
}
