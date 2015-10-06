package org.jutils.chart.data;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.jutils.chart.model.*;

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
    public final Chart chart;
    /**  */
    public final IAxisCoords domainCoords;
    /**  */
    public final IAxisCoords rangeCoords;
    /**  */
    public final IAxisCoords secDomainCoords;
    /**  */
    public final IAxisCoords secRangeCoords;

    /***************************************************************************
     * 
     **************************************************************************/
    public ChartContext( Chart chart )
    {
        this.chart = chart;

        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;

        this.domainCoords = new DomainDimensionCoords( chart.domainAxis, true );
        this.rangeCoords = new RangeDimensionCoords( chart.rangeAxis, true );
        this.secDomainCoords = new DomainDimensionCoords( chart.secDomainAxis,
            false );
        this.secRangeCoords = new RangeDimensionCoords( chart.secRangeAxis,
            false );
    }

    /***************************************************************************
     * @param chart
     **************************************************************************/
    public void calculateAutoBounds()
    {
        domainCoords.calculateBounds( chart.series );
        rangeCoords.calculateBounds( chart.series );
        secDomainCoords.calculateBounds( chart.series );
        secRangeCoords.calculateBounds( chart.series );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void restoreAutoBounds()
    {
        chart.domainAxis.calcBounds = true;
        chart.rangeAxis.calcBounds = true;
        chart.secDomainAxis.calcBounds = true;
        chart.secRangeAxis.calcBounds = true;

        latchCoords();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void latchCoords()
    {
        domainCoords.latchCoords( chart.domainAxis.getBounds(), width );
        rangeCoords.latchCoords( chart.rangeAxis.getBounds(), height );
        secDomainCoords.latchCoords( chart.secDomainAxis.getBounds(), width );
        secRangeCoords.latchCoords( chart.secRangeAxis.getBounds(), height );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static List<Interval> getIntervals( List<Series> series,
        boolean isDomain, boolean isPrimary )
    {
        List<Interval> intervals = new ArrayList<>( series.size() );

        for( Series s : series )
        {
            boolean isRequestedAxis = false;

            if( isDomain && isPrimary ) // primary domain
            {
                isRequestedAxis = s.isPrimaryDomain;
            }
            else if( isDomain && !isPrimary ) // secondary domain
            {
                isRequestedAxis = !s.isPrimaryDomain;
            }
            else if( !isDomain && isPrimary ) // primary range
            {
                isRequestedAxis = s.isPrimaryRange;
            }
            else if( !isDomain && !isPrimary ) // secondary range
            {
                isRequestedAxis = !s.isPrimaryRange;
            }

            if( s.visible && isRequestedAxis )
            {
                Interval span;

                if( isDomain )
                {
                    span = s.calcDomainSpan();
                }
                else
                {
                    span = s.calcRangeSpan();
                }

                intervals.add( span );
            }
        }

        return intervals;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isAutoBounds()
    {
        // TODO This is terrible fix it!!!!
        return chart.domainAxis.calcBounds && chart.rangeAxis.calcBounds &&
            chart.secDomainAxis.calcBounds && chart.secDomainAxis.calcBounds;
    }

    /***************************************************************************
     * @param p
     * @return
     **************************************************************************/
    public Point ensurePoint( Point p )
    {
        p.x = ensureHorizontal( p.x );
        p.y = ensureVertical( p.y );

        return p;
    }

    /***************************************************************************
     * @param x
     * @return
     **************************************************************************/
    private int ensureHorizontal( int x )
    {
        x = Math.max( x, this.x );
        x = Math.min( x, this.x + width );

        return x;
    }

    /***************************************************************************
     * @param y
     * @return
     **************************************************************************/
    private int ensureVertical( int y )
    {
        y = Math.max( y, this.y );
        y = Math.min( y, this.y + height );

        return y;
    }

    /***************************************************************************
     * Returns the bounds for the provided intervals that includes each interval
     * or {@code null} if the list is empty.
     * @param isPrimary
     * @param isDomain
     * @param intervals
     * @return
     **************************************************************************/
    private static Interval calculateAutoBounds( List<Series> series,
        boolean isDomain, boolean isPrimary )
    {
        List<Interval> intervals = getIntervals( series, isDomain, isPrimary );
        Double min = null;
        Double max = null;

        for( Interval span : intervals )
        {
            if( span == null )
            {
                continue;
            }

            if( min == null )
            {
                min = span.min;
                max = span.max;
            }
            else
            {
                min = Math.min( min, span.min );
                max = Math.max( max, span.max );
            }
        }

        if( min == null )
        {
            return null;
        }
        else if( min.equals( max ) )
        {
            min -= 0.5;
            max += 0.5;
        }

        double r = max - min;

        return new Interval( min - 0.03 * r, max + 0.03 * r );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static interface IAxisCoords
    {
        public double fromScreen( int s );

        public int fromCoord( double c );

        public Interval getBounds();

        public void calculateBounds( List<Series> series );

        public void latchCoords( Interval bounds, int length );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static abstract class AbrstractCoords implements IAxisCoords
    {
        private final Axis axis;
        private final boolean isDomain;
        private final boolean isPrimary;
        protected DimensionStats stats;

        public AbrstractCoords( Axis axis, boolean isDomain, boolean isPrimary )
        {
            this.axis = axis;
            this.isDomain = isDomain;
            this.isPrimary = isPrimary;
            this.stats = new DimensionStats( new Interval( -5, 5 ), 500 );
        }

        @Override
        public abstract double fromScreen( int s );

        @Override
        public abstract int fromCoord( double c );

        @Override
        public final Interval getBounds()
        {
            return stats.bounds;
        }

        @Override
        public final void calculateBounds( List<Series> series )
        {
            axis.autoBounds = calculateAutoBounds( series, isDomain,
                isPrimary );
        }

        @Override
        public final void latchCoords( Interval bounds, int length )
        {
            this.stats = new DimensionStats( bounds, length );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    static class DomainDimensionCoords extends AbrstractCoords
    {
        public DomainDimensionCoords( Axis axis, boolean isPrimary )
        {
            super( axis, true, isPrimary );
        }

        @Override
        public double fromScreen( int s )
        {
            return s / stats.scale + stats.bounds.min;
        }

        @Override
        public int fromCoord( double c )
        {
            return ( int )Math.round( ( c - stats.bounds.min ) * stats.scale );
            // return ( int )( ( c - stats.span.min ) * stats.scale );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class RangeDimensionCoords extends AbrstractCoords
    {
        public RangeDimensionCoords( Axis axis, boolean isPrimary )
        {
            super( axis, false, isPrimary );
        }

        @Override
        public double fromScreen( int s )
        {
            return stats.bounds.max - s / stats.scale;
        }

        @Override
        public int fromCoord( double c )
        {
            return ( int )Math.round(
                stats.length - ( c - stats.bounds.min ) * stats.scale );
            // return ( int )( stats.length - ( c - stats.span.min ) *
            // stats.scale );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class DimensionStats
    {
        public final Interval bounds;
        public final double scale;
        public final int length;

        public DimensionStats( Interval bounds, int length )
        {
            this.bounds = bounds;
            this.length = length;
            this.scale = bounds != null ? length / bounds.range : 0;
        }
    }
}
