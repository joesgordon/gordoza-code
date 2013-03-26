package org.jutils.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;
import org.jutils.IconConstants;

public class ByteCacheTest
{
    private static byte[] loadBytes( URL url ) throws IOException
    {
        InputStream stream = null;
        byte[] bytes;

        stream = url.openStream();

        try
        {
            bytes = new byte[stream.available()];

            stream.read( bytes );
        }
        finally
        {
            if( stream != null )
            {
                stream.close();
            }
        }

        return bytes;
    }

    private static byte[] loadTestBytes() throws IOException
    {
        URL url;

        url = IconConstants.loader.loader.getUrl( IconConstants.ATOMIC_32 );

        return loadBytes( url );
    }

    private byte[] getTestBytes()
    {
        try
        {
            return loadTestBytes();
        }
        catch( IOException ex )
        {
            throw new IllegalStateException(
                "Cannot perform test. Test bytes could not be loaded", ex );
        }
    }

    @Test
    public void testCreate()
    {
        ByteCache cache = new ByteCache();

        Assert.assertEquals( ByteCache.DEFAULT_SIZE, cache.getSize() );
    }

    @Test
    public void testCreateInt()
    {
        ByteCache cache = new ByteCache( 2 );

        Assert.assertEquals( 2, cache.getSize() );
    }

    @Test
    public void testCreateByteArray()
    {
        byte[] bytes = getTestBytes();
        ByteCache cache = new ByteCache( bytes );

        Assert.assertEquals( bytes.length, cache.getSize() );
    }

    @Test
    public void testRemainingRead()
    {
        ByteCache cache = new ByteCache();
        ByteArrayStream stream = null;
        byte[] bytes = getTestBytes();
        byte[] buf = new byte[10];

        try
        {
            stream = new ByteArrayStream( bytes );

            try
            {
                cache.readFromStream( stream );

                int value = Math.min( cache.getSize(), bytes.length );

                Assert.assertEquals( value, cache.remainingRead() );

                cache.read();
                value--;

                Assert.assertEquals( value, cache.remainingRead() );

                cache.read( buf, 0, buf.length );
                value -= buf.length;

                Assert.assertEquals( value, cache.remainingRead() );

                cache.write( ( byte )0 );
                value--;

                Assert.assertEquals( value, cache.remainingRead() );
            }
            finally
            {
                if( stream != null )
                {
                    stream.close();
                }
            }
        }
        catch( IOException ex )
        {
            Assert.fail( ex.getMessage() );
        }
    }
}
