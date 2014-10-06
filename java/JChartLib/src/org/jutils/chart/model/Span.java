package org.jutils.chart.model;

/*******************************************************************************
 *
 ******************************************************************************/
public class Span
{
    /**  */
    public final double min;
    /**  */
    public final double max;
    /**  */
    public final double range;

    /***************************************************************************
     * @param min
     * @param max
     **************************************************************************/
    public Span( double min, double max )
    {
        this.min = min;
        this.max = max;
        this.range = this.max - this.min;
    }

    /***************************************************************************
     * @param s
     **************************************************************************/
    public Span( Span s )
    {
        this.min = s.min;
        this.max = s.max;
        this.range = s.range;
    }
}
