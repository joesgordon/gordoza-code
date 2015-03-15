package org.jutils.chart.data;

public class Tick
{
    public final int offset;
    public final double value;
    public final String label;

    public Tick( int offset, double value )
    {
        this( offset, value, String.format( "%.3f", value ) );
    }

    public Tick( int offset, double value, String label )
    {
        this.offset = offset;
        this.value = value;
        this.label = label;
    }
}
