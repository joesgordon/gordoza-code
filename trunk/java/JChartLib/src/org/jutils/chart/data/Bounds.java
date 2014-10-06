package org.jutils.chart.data;

import org.jutils.chart.model.Span;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Bounds
{
    /**  */
    public Span primaryDomainSpan;
    /**  */
    public Span secondaryDomainSpan;
    /**  */
    public Span primaryRangeSpan;
    /**  */
    public Span secondaryRangeSpan;

    /***************************************************************************
     * 
     **************************************************************************/
    public Bounds()
    {
        this.primaryDomainSpan = new Span( -5.0, 5.0 );
        this.primaryRangeSpan = new Span( -5.0, 5.0 );
        this.secondaryDomainSpan = null;
        this.secondaryRangeSpan = null;
    }

    /***************************************************************************
     * @param bounds
     **************************************************************************/
    public Bounds( Bounds bounds )
    {
        this.primaryDomainSpan = nullCopy( bounds.primaryDomainSpan );
        this.secondaryDomainSpan = nullCopy( bounds.secondaryDomainSpan );
        this.primaryRangeSpan = nullCopy( bounds.primaryRangeSpan );
        this.secondaryRangeSpan = nullCopy( bounds.secondaryRangeSpan );
    }

    /***************************************************************************
     * @param s
     * @return
     **************************************************************************/
    private static Span nullCopy( Span s )
    {
        if( s == null )
        {
            return null;
        }

        return new Span( s );
    }
}
