package org.jutils.io;

import java.util.Random;

import junit.framework.Assert;

import org.junit.Test;
import org.jutils.io.BitBuffer;

public class BitBufferTest
{
    private static final byte [] fromArray;
    private static final byte [] toArray;

    static
    {
        fromArray = new byte[10 * ( 1 << 20 )];
        toArray = new byte[fromArray.length];

        new Random().nextBytes( fromArray );
    }

    @Test
    public void testNonZero()
    {
        for( int i = 0; i < fromArray.length; i++ )
        {
            if( fromArray[i] != 0 )
            {
                return;
            }
        }

        Assert.assertTrue( "The from array is all zeros", false );
    }

    @Test
    public void testRead()
    {
        BitBuffer from = new BitBuffer( fromArray );
        BitBuffer to = new BitBuffer( toArray );

        int bitCount = fromArray.length * 8;

        from.writeTo( to, bitCount );

        for( int i = 0; i < fromArray.length; i++ )
        {
            Assert.assertEquals( fromArray[i], toArray[i] );
        }
    }
}
