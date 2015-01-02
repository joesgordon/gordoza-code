package org.jutils.chart.data;

import java.util.Objects;

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

    /***************************************************************************
     * @param s
     * @return
     **************************************************************************/
    @Override
    public boolean equals( Object obj )
    {
        if( obj == null )
        {
            return false;
        }

        if( obj instanceof Bounds )
        {
            Bounds that = ( Bounds )obj;

            if( !that.primaryDomainSpan.equals( primaryDomainSpan ) )
            {
                return false;
            }
            else if( !that.primaryRangeSpan.equals( primaryRangeSpan ) )
            {
                return false;
            }
            else if( that.secondaryDomainSpan != null &&
                secondaryDomainSpan != null &&
                !that.secondaryDomainSpan.equals( secondaryDomainSpan ) )
            {
                return false;
            }
            else if( that.secondaryDomainSpan != null &&
                secondaryDomainSpan == null )
            {
                return false;
            }
            else if( that.secondaryDomainSpan == null &&
                secondaryDomainSpan != null )
            {
                return false;
            }
            else if( that.secondaryRangeSpan != null &&
                secondaryRangeSpan != null &&
                !that.secondaryRangeSpan.equals( secondaryRangeSpan ) )
            {
                return false;
            }
            else if( that.secondaryRangeSpan != null &&
                secondaryRangeSpan == null )
            {
                return false;
            }
            else if( that.secondaryRangeSpan == null &&
                secondaryRangeSpan != null )
            {
                return false;
            }
            else
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( primaryDomainSpan, primaryRangeSpan,
            secondaryDomainSpan, secondaryRangeSpan );
    }
}
