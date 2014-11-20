package org.jutils.chart.data;

import org.jutils.chart.model.Chart;
import org.jutils.chart.model.Span;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChartContext
{
    /**  */
    public int x;
    /**  */
    public int y;
    /**  */
    public int width;
    /**  */
    public int height;

    /**  */
    private Bounds bounds;
    /**  */
    private Bounds autoBounds;

    /**  */
    public AxisCoords domain;
    /**  */
    public AxisCoords range;

    /***************************************************************************
     * 
     **************************************************************************/
    public ChartContext()
    {
        x = 0;
        y = 0;
        width = 0;
        height = 0;

        this.bounds = new Bounds();
        this.autoBounds = new Bounds();
    }

    /***************************************************************************
     * @param chart
     **************************************************************************/
    public void setAutoBounds( Chart chart )
    {
        calculateAutoBounds( chart );

        bounds = new Bounds( autoBounds );

        latchCoords();
    }

    /***************************************************************************
     * @param chart
     **************************************************************************/
    public void calculateAutoBounds( Chart chart )
    {
        autoBounds.primaryDomainSpan = chart.calculatePrimaryDomainSpan();
        autoBounds.primaryRangeSpan = chart.calculatePrimaryRangeSpan();
        autoBounds.secondaryDomainSpan = chart.calculateSecondaryDomainSpan();
        autoBounds.secondaryRangeSpan = chart.calculateSecondaryRangeSpan();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void restoreAutoBounds()
    {
        bounds = new Bounds( autoBounds );

        latchCoords();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void latchCoords()
    {
        this.domain = new AxisCoords( width, true, bounds.primaryDomainSpan,
            bounds.secondaryDomainSpan );
        this.range = new AxisCoords( height, false, bounds.primaryRangeSpan,
            bounds.secondaryRangeSpan );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Bounds getBounds()
    {
        return new Bounds( bounds );
    }

    /***************************************************************************
     * @param b
     **************************************************************************/
    public void setBounds( Bounds b )
    {
        this.bounds = new Bounds( b );

        latchCoords();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isAutoBounds()
    {
        return bounds.equals( autoBounds );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static class AxisCoords
    {
        public final IDimensionCoords primary;
        public final IDimensionCoords secondary;

        public AxisCoords( int length, boolean isDomain, Span primarySpan,
            Span secondarySpan )
        {
            IDimensionCoords primary;
            IDimensionCoords secondary = null;

            if( isDomain )
            {
                DimensionStats stats;

                stats = new DimensionStats( primarySpan, length );
                primary = new DomainDimensionCoords( stats );

                if( secondarySpan != null )
                {
                    stats = new DimensionStats( secondarySpan, length );
                    secondary = new DomainDimensionCoords( stats );
                }
            }
            else
            {
                DimensionStats stats;

                stats = new DimensionStats( primarySpan, length );
                primary = new RangeDimensionCoords( stats );

                if( secondarySpan != null )
                {
                    stats = new DimensionStats( secondarySpan, length );
                    secondary = new RangeDimensionCoords( stats );
                }
            }

            this.primary = primary;
            this.secondary = secondary;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class DimensionStats
    {
        public final Span span;
        public final double scale;
        public final int length;

        public DimensionStats( Span span, int length )
        {
            this.span = span;
            this.length = length;
            this.scale = length / span.range;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static interface IDimensionCoords
    {
        public double fromScreen( int s );

        public int fromCoord( double c );

        public Span getSpan();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class DomainDimensionCoords implements IDimensionCoords
    {
        public final DimensionStats stats;

        public DomainDimensionCoords( DimensionStats stats )
        {
            this.stats = stats;
        }

        @Override
        public double fromScreen( int s )
        {
            return s / stats.scale + stats.span.min;
        }

        @Override
        public int fromCoord( double c )
        {
            // return ( int )Math.round( ( c - stats.span.min ) * stats.scale );
            return ( int )( ( c - stats.span.min ) * stats.scale );
        }

        @Override
        public Span getSpan()
        {
            return stats.span;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class RangeDimensionCoords implements IDimensionCoords
    {
        public final DimensionStats stats;

        public RangeDimensionCoords( DimensionStats stats )
        {
            this.stats = stats;
        }

        @Override
        public double fromScreen( int s )
        {
            return -1 * s / stats.scale + stats.span.max;
        }

        @Override
        public int fromCoord( double c )
        {
            // return ( int )Math.round( stats.length - ( c - stats.span.min ) *
            // stats.scale );
            return ( int )( stats.length - ( c - stats.span.min ) * stats.scale );
        }

        @Override
        public Span getSpan()
        {
            return stats.span;
        }
    }
}
