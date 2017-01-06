package org.jutils.io.bits;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import org.jutils.io.*;

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
        final byte [] buffer = buildBytes( 42 );

        final byte [] expected = buildExpected( buffer, f, t );

        byte [] actual = new byte[buffer.length];

        BitBuffer fromBitBuf = new BitBuffer( buffer );
        BitBuffer toBitBuf = new BitBuffer( actual );

        fromBitBuf.setPosition( 0, f );
        toBitBuf.setPosition( 0, t );

        shifter.shift( fromBitBuf, toBitBuf, buffer.length - 1 );

        Assert.assertArrayEquals( expected, actual );

        Assert.assertEquals( new BitPosition( buffer.length - 1, t ),
            toBitBuf.getPosition() );
    }

    private static byte [] buildBytes( int count )
    {
        byte [] buffer = new byte[count];

        Random r = new Random( 42 );
        r.nextBytes( buffer );

        return buffer;
    }

    private static byte [] buildExpected( byte [] buffer, int fromBit,
        int toBit )
    {
        byte [] expected = new byte[buffer.length];
        BitBuffer bb = new BitBuffer( buffer );
        BitBuffer bb2 = new BitBuffer( expected );

        bb.setPosition( 0, fromBit );
        bb2.setPosition( 0, toBit );

        bb.writeTo( bb2, ( buffer.length - 1 ) * 8 );

        return expected;
    }
}
