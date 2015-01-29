package org.jutils.utils;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;
import org.jutils.io.*;
import org.jutils.utils.CachedList.ICacher;

/*******************************************************************************
 * 
 ******************************************************************************/
public class CachedListTest
{
    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testAddSizeMinusOne()
    {
        final int count = 8;
        final int testCount = 7;

        testNAdditions( count, testCount );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testAddExactSize()
    {
        final int count = 8;
        final int testCount = 8;

        testNAdditions( count, testCount );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testAddSizePlusOne()
    {
        final int count = 8;
        final int testCount = 9;

        testNAdditions( count, testCount );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testAddTwiceSizeMinusOne()
    {
        final int count = 8;
        final int testCount = 15;

        testNAdditions( count, testCount );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testAddTwiceExactSize()
    {
        final int count = 8;
        final int testCount = 16;

        testNAdditions( count, testCount );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testAddTwiceSizePlusOne()
    {
        final int count = 8;
        final int testCount = 17;

        testNAdditions( count, testCount );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testAddSizePlusOneThenGet()
    {
        final int count = 8;
        final int testCount = 9;

        try( ByteArrayStream stream = new ByteArrayStream() )
        {
            ICacher<Integer> cacher = createCacher();
            CachedList<Integer> list = new CachedList<>( cacher, stream, count );

            for( int i = 0; i < testCount; i++ )
            {
                list.add( i );
            }

            int i = list.get( 1 );
            Assert.assertEquals( 1, i );

            list.add( testCount );
            i = list.get( testCount );
            Assert.assertEquals( testCount, i );

            i = list.get( 1 );
            Assert.assertEquals( 1, i );
        }
        catch( IOException ex )
        {
            Assert.fail( "I/O Exception: " + ex.getMessage() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testAdd32ThenGet32()
    {
        final int count = 8;
        final int testCount = 33;

        try( ByteArrayStream stream = new ByteArrayStream() )
        {
            ICacher<Integer> cacher = createCacher();
            CachedList<Integer> list = new CachedList<>( cacher, stream, count );

            for( int i = 0; i < testCount; i++ )
            {
                list.add( i );
            }

            for( int i = 0; i < testCount; i++ )
            {
                int val = list.get( i );
                Assert.assertEquals( i, val );
            }
        }
        catch( IOException ex )
        {
            Assert.fail( "I/O Exception: " + ex.getMessage() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static void testNAdditions( int count, int testCount )
    {
        try( ByteArrayStream stream = new ByteArrayStream() )
        {
            ICacher<Integer> cacher = createCacher();
            CachedList<Integer> list = new CachedList<>( cacher, stream, count );

            for( int i = 0; i < testCount; i++ )
            {
                list.add( i );
            }

            Assert.assertEquals( testCount, list.size() );

            int cacheCount = ( testCount - 1 ) / count;
            int cacheSize = count * cacher.getItemSize();
            long expectedSize = cacheCount * cacheSize;

            Assert.assertEquals( "count: " + cacheCount + ", size: " +
                cacheSize + ", #: " + list.size(), expectedSize,
                stream.getLength() );
        }
        catch( IOException ex )
        {
            Assert.fail( "I/O Exception: " + ex.getMessage() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static ICacher<Integer> createCacher()
    {
        return new IntegerCacher();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class IntegerCacher implements ICacher<Integer>
    {
        private final byte [] bytes;
        private final ByteBuffer buffer;

        public IntegerCacher()
        {
            this.bytes = new byte[4];
            this.buffer = ByteBuffer.wrap( bytes );
        }

        @Override
        public Integer read( IStream stream ) throws IOException,
            RuntimeFormatException
        {
            buffer.rewind();

            stream.read( bytes );

            return buffer.getInt();
        }

        @Override
        public void write( Integer item, IStream stream ) throws IOException
        {
            buffer.rewind();

            buffer.putInt( item );

            stream.write( bytes );
        }

        @Override
        public int getItemSize()
        {
            return bytes.length;
        }

        @Override
        public void reportException( IOException ex )
        {
            Assert.fail( "I/O Exception: " + ex.getMessage() );
        }
    }
}
