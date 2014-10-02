package org.jutils.chart.model;

import org.jutils.chart.data.XYPoint;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Series
{
    public String name;
    public final ISeriesData data;
    public boolean visible;
    public final MarkerStyle marker = new MarkerStyle();
    public final MarkerStyle highlight = new MarkerStyle();
    public final LineStyle line = new LineStyle();
    public boolean isPrimaryDomain;
    public boolean isPrimaryRange;

    public Series( ISeriesData data )
    {
        this.data = data;

        if( data.getCount() == 0 )
        {
            throw new IllegalArgumentException( "The series contains no data" );
        }
    }

    public Span calcDomainSpan()
    {
        Double min = null;
        Double max = null;

        for( XYPoint p : data )
        {
            if( !p.hidden && !Double.isNaN( p.x ) )
            {
                if( min == null )
                {
                    min = p.x;
                    max = p.x;
                }
                else
                {
                    min = Math.min( min, p.x );
                    max = Math.max( max, p.x );
                }
            }
        }

        return new Span( min, max );
    }

    public Span calcRangeSpan()
    {
        Double min = null;
        Double max = null;

        for( XYPoint p : data )
        {
            if( !p.hidden && !Double.isNaN( p.y ) )
            {
                if( min == null )
                {
                    min = p.y;
                    max = p.y;
                }
                else
                {
                    min = Math.min( min, p.y );
                    max = Math.max( max, p.y );
                }
            }
        }

        return new Span( min, max );
    }
}
