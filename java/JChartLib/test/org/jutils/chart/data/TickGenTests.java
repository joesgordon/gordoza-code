package org.jutils.chart.data;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.jutils.chart.data.TickGen.TickMetrics;
import org.jutils.chart.model.Axis;
import org.jutils.chart.model.Interval;
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
        TickGen gen = new TickGen();
        List<Tick> ticks = new ArrayList<>();
        int width = 650;

        // bounds = new Interval( -6.0, 5.0 );
        // bounds = new Interval( 12.0, 50824.0 );
        // bounds = new Interval( 6.593, 6.9759 );
        // bounds = new Interval( -8e32, -8e32 );
        axis.setBounds( new Interval( 19, 21 ) );
        gen.genTicks( axis, width, ticks );

        LogUtils.printDebug( "Tick count: " + ticks.size() );
        for( Tick t : ticks )
        {
            LogUtils.printDebug( "Tick @ " + t.value + " : " + t.label );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testgenerateTickMetrics()
    {
        TickGen gen = new TickGen();

        Interval s = new Interval( 8e-32, 8e-32 );
        TickMetrics tm = gen.generateTickMetrics( 5 * 96, s );

        Assert.assertEquals( 3, tm.tickCount );
    }
}
