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
    public Span primaryDomainSpan;
    /**  */
    public Span secondaryDomainSpan;
    /**  */
    public Span primaryRangeSpan;
    /**  */
    public Span secondaryRangeSpan;

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
    }

    /***************************************************************************
     * @param chart
     **************************************************************************/
    public void calculate( Chart chart )
    {
        primaryDomainSpan = chart.calculatePrimaryDomainSpan();
        primaryRangeSpan = chart.calculatePrimaryRangeSpan();
        secondaryDomainSpan = chart.calculateSecondaryDomainSpan();
        secondaryRangeSpan = chart.calculateSecondaryRangeSpan();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void latchCoords()
    {
        this.domain = new AxisCoords( width, true, primaryDomainSpan,
            secondaryDomainSpan );
        this.range = new AxisCoords( height, false, primaryRangeSpan,
            secondaryRangeSpan );
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
            return -1 * s / stats.scale + stats.span.min;
        }

        @Override
        public int fromCoord( double c )
        {
            return ( int )( stats.length - ( c - stats.span.min ) * stats.scale );
        }

        @Override
        public Span getSpan()
        {
            return stats.span;
        }
    }
}
