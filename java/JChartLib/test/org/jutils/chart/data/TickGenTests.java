package org.jutils.chart.data;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.jutils.chart.data.TickGen.TickMetrics;
import org.jutils.chart.model.Axis;
import org.jutils.chart.model.Span;
import org.jutils.io.LogUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TickGenTests
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testGenTicksPrint()
    {
        Axis axis = new Axis();
        TickGen gen = new TickGen( axis );
        ChartContext context;
        List<Tick> ticks;

        // context = createContext( -6.0, 5.0, 1600 );
        // context = createContext( 12.0, 50824.0, 600 );
        // context = createContext( 6.593, 6.9759, 700 );
        // context = createContext( -8e32, -8e32, 700 );
        context = createContext( 19, 21, 650 );
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
    private static ChartContext createContext( double min, double max,
        int size )
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
     * 
     **************************************************************************/
    @Test
    public void testgenerateTickMetrics()
    {
        Axis axis = new Axis();
        TickGen gen = new TickGen( axis );

        Span s = new Span( 8e-32, 8e-32 );
        TickMetrics tm = gen.generateTickMetrics( 5 * 96, s );

        Assert.assertEquals( 3, tm.tickCount );
    }
}
