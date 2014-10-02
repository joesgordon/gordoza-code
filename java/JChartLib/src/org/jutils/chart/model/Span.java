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
        double r = max - min;
        this.min = min - 0.003 * r;
        this.max = max + 0.003 * r;
        this.range = this.max - this.min;
    }
}
