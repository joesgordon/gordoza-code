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

    public static void min( XYPoint xy1, XYPoint xy2, XYPoint result )
    {
        result.x = Math.min( xy1.x, xy2.x );
        result.y = Math.min( xy1.y, xy2.y );
    }

    public static void max( XYPoint xy1, XYPoint xy2, XYPoint result )
    {
        result.x = Math.max( xy1.x, xy2.x );
        result.y = Math.max( xy1.y, xy2.y );
    }
}
