package org.jutils.ui.hex;

import junit.framework.Assert;

import org.junit.Test;
import org.jutils.NumberParsingUtils;

public class HexUtilsTest
{
    @Test
    public void testToUnsigned()
    {
        for( int b = Byte.MIN_VALUE; b <= Byte.MAX_VALUE; b++ )
        {
            int i = HexUtils.toUnsigned( ( byte )b );

            if( b < 0 )
            {
                Assert.assertEquals( b + 256, i );
            }
            else
            {
                Assert.assertEquals( b, i );
            }
        }
    }

    @Test
    public void testStuff()
    {
        int max = 10000000;
        long start = System.nanoTime();
        String str;
        long num;

        for( int i = -max; i < max; i++ )
        {
            str = Long.toHexString( i );
            // num = Long.parseLong( str, 16 );
            num = NumberParsingUtils.parseHexLong( str );
            // num = new BigInteger( str, 16 ).longValue();
            // num = parseHexLong2( str );

            if( num != i )
            {
                Assert.assertEquals( i, num );
            }
        }
        long estTime = System.nanoTime() - start;
        double sec = estTime / 1000000000.0;
        System.out.println( String.format( "Took: %3fs", sec ) );
    }

    @Test
    public void testToSigned()
    {
        for( int i = 0; i <= 256; i++ )
        {
            byte b = HexUtils.toSigned( i );

            if( i > Byte.MAX_VALUE )
            {
                Assert.assertEquals( i, b + 256 );
            }
            else
            {
                Assert.assertEquals( i, b );
            }
        }
    }

}
