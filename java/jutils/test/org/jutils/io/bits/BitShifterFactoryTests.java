package org.jutils.io.bits;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import org.jutils.io.BitBuffer;
import org.jutils.io.LogUtils;

public class BitShifterFactoryTests
{
    @Test
    public void testCreateFromMask()
    {
        int mask = BitShifterFactory.createFromMask( 7, 0 );
        Assert.assertEquals( 0xFE, mask );
    }

    @Test
    public void testCreateToMask()
    {
        int mask = BitShifterFactory.createToMask( 7, 1 );
        Assert.assertEquals( 0x80, mask );
    }

    @Test
    public void testFactory()
    {
        BitShifterFactory factory = new BitShifterFactory();

        for( int f = 0; f < 8; f++ )
        {
            for( int t = 0; t < 8; t++ )
            {
                IBitShifter shifter = factory.getShifter( f, t );

                LogUtils.printDebug( "Testing %d -> %d", f, t );

                testShifter( shifter, f, t );
            }
        }
    }

    private static void testShifter( IBitShifter shifter, int f, int t )
    {
        byte [] buffer = new byte[40];
        Random r = new Random( 42 );
        r.nextBytes( buffer );

        byte [] expected = new byte[buffer.length];
        BitBuffer bb = new BitBuffer( buffer );
        BitBuffer bb2 = new BitBuffer( expected );

        bb.setPosition( 0, f );
        bb2.setPosition( 0, t );

        bb.writeTo( bb2, ( buffer.length - 1 ) * 8 );

        byte [] actual = new byte[buffer.length];

        bb = new BitBuffer( buffer );
        bb2 = new BitBuffer( actual );

        shifter.shift( bb, bb2, buffer.length - 1 );

        Assert.assertArrayEquals( expected, actual );
    }
}
