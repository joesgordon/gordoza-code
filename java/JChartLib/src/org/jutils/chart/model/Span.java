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
        if( min == -0.0 && max == 0.0 )
        {
            min = -0.0001;
            max = 0.0001;
        }

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

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits( max );
        result = prime * result + ( int )( temp ^ ( temp >>> 32 ) );
        temp = Double.doubleToLongBits( min );
        result = prime * result + ( int )( temp ^ ( temp >>> 32 ) );
        return result;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean equals( Object obj )
    {
        if( this == obj )
            return true;
        if( obj == null )
            return false;
        if( getClass() != obj.getClass() )
            return false;
        Span other = ( Span )obj;
        if( Double.doubleToLongBits( max ) != Double.doubleToLongBits( other.max ) )
            return false;
        if( Double.doubleToLongBits( min ) != Double.doubleToLongBits( other.min ) )
            return false;
        return true;
    }
}
