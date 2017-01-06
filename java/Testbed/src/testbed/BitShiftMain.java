package testbed;

import java.util.Arrays;
import java.util.Random;

import org.jutils.Stopwatch;
import org.jutils.io.*;
import org.jutils.io.bits.BitShifterFactory;
import org.jutils.io.bits.IBitShifter;
import org.jutils.time.TimeUtils;
import org.jutils.utils.RunningStat;
import org.jutils.utils.RunningStat.Stats;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BitShiftMain
{
    private static final int BUF_SIZE = 1024 * 128;
    private static final int ITERATION_CNT = 100;

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String[] args )
    {
        // BitShifterFactory.printMetrics();

        testAllShifts();

        // testShift( 1, 1 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static void testAllShifts()
    {
        byte[] src = buildBytes( BUF_SIZE );
        byte[] dst = new byte[src.length];

        long oldTime = 0L;
        long newTime = 0L;

        BitBuffer srcBuf = new BitBuffer( src );
        BitBuffer dstBuf = new BitBuffer( dst );

        Stopwatch watch = new Stopwatch();
        BitShifterFactory factory = new BitShifterFactory();

        int idx = 1;
        for( int s = 0; s < 8; s++ )
        {
            for( int d = 0; d < 8; d++ )
            {
                ShiftResults r = testShift( srcBuf, dstBuf, watch, s, d,
                    factory );
                long memoryTotal = Runtime.getRuntime().totalMemory();
                long memoryFree = Runtime.getRuntime().freeMemory();
                long memoryUsed = memoryTotal - memoryFree;
                LogUtils.printInfo(
                    "Testing %02d of 64: %s | Memory Used: %s of %s", idx++,
                    r.toString(), IOUtils.byteCount( memoryUsed ),
                    IOUtils.byteCount( memoryTotal ) );

                oldTime += r.oldStats.mean;
                newTime += r.newStats.mean;
            }
        }

        LogUtils.printInfo( "Old Time: %s",
            TimeUtils.durationToString( oldTime ) );
        LogUtils.printInfo( "New Time: %s",
            TimeUtils.durationToString( newTime ) );

        LogUtils.printInfo( ShiftResults.toString( oldTime, newTime ) );
    }

    /***************************************************************************
     * @param from
     * @param to
     **************************************************************************/
    @SuppressWarnings( "unused")
    private static void testShift( int from, int to )
    {
        byte[] src = buildBytes( BUF_SIZE );
        byte[] dst = new byte[src.length];
        BitBuffer srcBuf = new BitBuffer( src );
        BitBuffer dstBuf = new BitBuffer( dst );
        Stopwatch watch = new Stopwatch();
        BitShifterFactory factory = new BitShifterFactory();

        ShiftResults res = testShift( srcBuf, dstBuf, watch, from, to,
            factory );
        LogUtils.printInfo( "Tested : %s", res.toString() );
    }

    /***************************************************************************
     * @param srcBuf
     * @param dstBuf
     * @param watch
     * @param src
     * @param dst
     * @param idx
     * @param factory
     * @return
     **************************************************************************/
    private static ShiftResults testShift( BitBuffer srcBuf, BitBuffer dstBuf,
        Stopwatch watch, int src, int dst, BitShifterFactory factory )
    {
        IBitShifter shifter = factory.getShifter( src, dst );

        Stats os = runOld( srcBuf, dstBuf, watch, src, dst );

        // Runtime.getRuntime().gc();

        Stats ns = runNew( srcBuf, dstBuf, watch, src, dst, shifter );

        // Runtime.getRuntime().gc();

        return new ShiftResults( os, ns, src, dst );
    }

    /***************************************************************************
     * @param srcBuf
     * @param dstBuf
     * @param watch
     * @param s
     * @param d
     * @return
     **************************************************************************/
    private static Stats runOld( BitBuffer srcBuf, BitBuffer dstBuf,
        Stopwatch watch, int s, int d )
    {
        RunningStat rstats = new RunningStat();

        for( int i = 0; i < ITERATION_CNT; i++ )
        {
            srcBuf.setPosition( 0, s );
            dstBuf.setPosition( 0, d );
            Arrays.fill( dstBuf.buffer, ( byte )0 );
            watch.start();
            srcBuf.writeTo( dstBuf, ( srcBuf.buffer.length - 1 ) * 8 );
            watch.stop();
            rstats.account( watch.getElapsed() );
        }

        return rstats.calcStats();
    }

    /***************************************************************************
     * @param srcBuf
     * @param dstBuf
     * @param watch
     * @param s
     * @param d
     * @param shifter
     * @return
     **************************************************************************/
    private static Stats runNew( BitBuffer srcBuf, BitBuffer dstBuf,
        Stopwatch watch, int s, int d, IBitShifter shifter )
    {
        RunningStat rstats = new RunningStat();

        for( int i = 0; i < ITERATION_CNT; i++ )
        {
            srcBuf.setPosition( 0, s );
            dstBuf.setPosition( 0, d );
            Arrays.fill( dstBuf.buffer, ( byte )0 );
            watch.start();
            shifter.shift( srcBuf, dstBuf, srcBuf.buffer.length - 1 );
            watch.stop();
            rstats.account( watch.getElapsed() );
        }

        return rstats.calcStats();
    }

    /***************************************************************************
     * @param count
     * @return
     **************************************************************************/
    private static byte[] buildBytes( int count )
    {
        byte[] buffer = new byte[count];

        Random r = new Random( 42 );
        r.nextBytes( buffer );

        return buffer;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class ShiftResults
    {
        public final Stats oldStats;
        public final Stats newStats;
        public final int from;
        public final int to;

        public ShiftResults( Stats oldStats, Stats newStats, int from, int to )
        {
            this.oldStats = oldStats;
            this.newStats = newStats;
            this.from = from;
            this.to = to;
        }

        @Override
        public String toString()
        {
            long oldTime = ( long )oldStats.mean;
            long newTime = ( long )newStats.mean;

            return String.format( "(%d -> %d) %s", from, to,
                toString( oldTime, newTime ) );
        }

        public static String toString( long oldTime, long newTime )
        {
            long delta = Math.abs( oldTime - newTime );
            boolean newWon = newTime < oldTime;
            long losingTime = newWon ? oldTime : newTime;
            String winner = newWon ? "New" : "Old";
            double percent = 100.0 * delta / losingTime;

            return String.format( "%s won by %3d%%, old: %s, new: %s", winner,
                ( int )percent, TimeUtils.durationToString( oldTime ),
                TimeUtils.durationToString( newTime ) );
        }
    }
}
