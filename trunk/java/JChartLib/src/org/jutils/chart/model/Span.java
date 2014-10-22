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

    /***************************************************************************
     * @return
     **************************************************************************/
    public Span zoomIn()
    {
        double r;
        double min;
        double max;

        r = range / 3.0;
        min = this.min + r;
        max = this.max - r;
        return new Span( min, max );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Span zoomOut()
    {
        double r;
        double min;
        double max;

        r = range;
        min = this.min - r;
        max = this.max + r;
        return new Span( min, max );
    }
}
