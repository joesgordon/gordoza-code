package org.jutils.chart.data;

import java.util.*;

import org.jutils.chart.data.ChartContext.IDimensionCoords;
import org.jutils.chart.model.Axis;
import org.jutils.chart.model.Span;
import org.jutils.io.LogUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TickGen
{
    private final Axis axis;

    private int minTickSize;
    private int maxTickSize;

    /***************************************************************************
     * 
     **************************************************************************/
    public TickGen( Axis axis )
    {
        this.axis = axis;
        this.minTickSize = 24;
        this.maxTickSize = 96;
    }

    /***************************************************************************
     * @param offset
     * @param dist
     * @param coords
     * @param range
     * @return
     **************************************************************************/
    public List<Tick> genTicks( int offset, int dist, IDimensionCoords coords,
        boolean range )
    {
        List<Tick> ticks = new ArrayList<>();

        if( coords == null )
        {
            return ticks;
        }

        TickMetrics tickMets = new TickMetrics();

        if( axis.autoTicks )
        {
            tickMets = generateTickMetrics( dist, coords );
        }
        else
        {
            tickMets = calculateMetrics();
        }

        String fmt = tickMets.tickOrder > -1 ? "%.0f" : "%." +
            Math.abs( tickMets.tickOrder ) + "f";

        for( int i = 0; i < tickMets.tickCount; i++ )
        {
            double d = tickMets.tickStart + tickMets.tickWidth * i;
            int off = offset + coords.fromCoord( d );

            ticks.add( new Tick( off, d, String.format( fmt, d ) ) );
        }

        if( range )
        {
            Collections.reverse( ticks );
        }

        return ticks;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private TickMetrics calculateMetrics()
    {
        TickMetrics metrics = new TickMetrics();

        metrics.tickStart = axis.tickStart;
        metrics.tickWidth = axis.tickWidth;
        metrics.tickCount = ( int )( ( axis.tickEnd - axis.tickStart ) / axis.tickWidth ) + 1;
        metrics.tickOrder = ( int )Math.floor( Math.log10( metrics.tickWidth ) );

        return metrics;
    }

    /***************************************************************************
     * @param dist
     * @param coords
     * @return
     **************************************************************************/
    public TickMetrics generateTickMetrics( int dist, IDimensionCoords coords )
    {
        TickMetrics metrics = new TickMetrics();
        Span span = coords.getSpan();

        double minTickPx = dist / ( double )maxTickSize;
        double maxTickPx = dist / ( double )minTickSize;

        double minTickCs = span.range / maxTickPx;
        double maxTickCs = span.range / minTickPx;

        int minTickCsOrder = ( int )Math.floor( Math.log10( minTickCs ) );

        int minTickCsNorm = ( int )Math.floor( minTickCs * 10 /
            Math.pow( 10, minTickCsOrder ) );
        int maxTickCsNorm = ( int )Math.floor( maxTickCs * 10 /
            Math.pow( 10, minTickCsOrder ) );

        int tickWidthCsNorm = 0;

        if( 100 >= minTickCsNorm && 100 <= maxTickCsNorm )
        {
            tickWidthCsNorm = 100;
        }
        else if( 50 >= minTickCsNorm && 50 <= maxTickCsNorm )
        {
            tickWidthCsNorm = 50;
        }
        else if( 20 >= minTickCsNorm && 20 <= maxTickCsNorm )
        {
            tickWidthCsNorm = 20;
        }
        else if( 25 >= minTickCsNorm && 25 <= maxTickCsNorm )
        {
            tickWidthCsNorm = 25;
        }
        else if( 75 >= minTickCsNorm && 75 <= maxTickCsNorm )
        {
            tickWidthCsNorm = 75;
        }
        else
        {
            tickWidthCsNorm = 10 * ( int )Math.ceil( minTickCsNorm / 10.0 );
        }

        double tickWidthCs = tickWidthCsNorm * Math.pow( 10, minTickCsOrder ) /
            10.0;

        int tickCsOrder = ( int )Math.floor( Math.log10( tickWidthCs ) );

        double minCsNorm = span.min * 10 / Math.pow( 10, tickCsOrder );
        double maxCsNorm = span.max * 10 / Math.pow( 10, tickCsOrder );

        int addend = 10 * ( int )( tickWidthCs / Math.pow( 10, tickCsOrder ) );

        int minTickNorm = addend * ( int )Math.floor( minCsNorm / addend );
        int maxTickNorm = addend * ( int )Math.floor( maxCsNorm / addend );

        double tickStart = minTickNorm < minCsNorm ? minTickNorm + addend
            : minTickNorm;
        double tickStop = maxTickNorm > maxCsNorm ? maxTickNorm - addend
            : maxTickNorm;

        tickStart = tickStart / 10 * Math.pow( 10, tickCsOrder );
        tickStop = tickStop / 10 * Math.pow( 10, tickCsOrder );

        metrics.tickCount = ( int )( Math.round( ( tickStop - tickStart ) /
            tickWidthCs ) ) + 1;
        metrics.tickOrder = tickCsOrder;
        metrics.tickStart = tickStart;
        metrics.tickWidth = tickWidthCs;

        return metrics;
    }

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        Axis axis = new Axis();
        TickGen gen = new TickGen( axis );
        ChartContext context;
        List<Tick> ticks;

        // context = createContext( -6.0, 5.0, 1600 );
        // context = createContext( 12.0, 50824.0, 600 );
        context = createContext( 6.593, 6.9759, 700 );
        ticks = gen.genTicks( 0, context.width, context.domain.primary, false );

        LogUtils.printDebug( "Tick count: " + ticks.size() );
        for( Tick t : ticks )
        {
            LogUtils.printDebug( "Tick @ " + t.offset + " : " + t.value );
        }
    }

    /***************************************************************************
     * @param min
     * @param max
     * @param size
     * @return
     **************************************************************************/
    private static ChartContext createContext( double min, double max, int size )
    {
        ChartContext context = new ChartContext();
        Bounds b = new Bounds();

        b.primaryDomainSpan = new Span( min, max );
        b.primaryRangeSpan = new Span( 5.0, 50.0 );
        context.setBounds( b );
        context.width = size;
        context.height = 600;
        context.latchCoords();

        return context;
    }

    /***************************************************************************
     * @param offset
     * @param dist
     * @param sectionCount
     * @param min
     * @param max
     * @return
     **************************************************************************/
    public static List<Tick> genTicks( int offset, int dist, int sectionCount,
        IDimensionCoords coords, boolean range )
    {
        List<Tick> ticks = new ArrayList<>();

        if( coords == null )
        {
            return ticks;
        }

        Span span = coords.getSpan();

        ticks.add( new Tick( offset, range ? span.max : span.min ) );

        for( int i = 1; i < sectionCount; i++ )
        {
            double d = range ? span.max - i * span.range / sectionCount
                : span.min + i * span.range / sectionCount;
            int off = offset + coords.fromCoord( d );
            ticks.add( new Tick( off, d ) );
        }

        ticks.add( new Tick( offset + dist, range ? span.min : span.max ) );

        return ticks;
    }

    private class TickMetrics
    {
        public double tickWidth;
        public double tickStart;

        public int tickCount;
        public int tickOrder;

    }
}
