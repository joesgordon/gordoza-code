package org.jutils.io;

import java.io.IOException;
import java.io.InputStream;

import org.junit.*;
import org.jutils.IconConstants;

public class BufferedStreamTest
{
    private ByteArrayStream byteStream;
    private byte[] bytes;

    private byte[] loadTestBytes() throws IOException
    {
        InputStream stream = IconConstants.loader.loader.getUrl(
            IconConstants.ATOMIC_32 ).openStream();
        byte[] bytes = new byte[stream.available()];

        stream.read( bytes );
        stream.close();

        return bytes;
    }

    @Before
    public void setUp()
    {
        try
        {
            bytes = loadTestBytes();
            byteStream = new ByteArrayStream( bytes );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
            Assert.fail( ex.getMessage() );
        }
    }

    @After
    public void tearDown()
    {
        try
        {
            byteStream.close();
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
            Assert.fail( ex.getMessage() );
        }
    }

    @Test
    public void testRead()
    {
        BufferedStream stream = new BufferedStream( byteStream );

        try
        {
            byte b = stream.read();
            Assert.assertEquals( bytes[0], b );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
            Assert.fail( ex.getMessage() );
        }
    }

    @Test
    public void testReadByteArray()
    {
        BufferedStream stream = new BufferedStream( byteStream, 1087 );

        try
        {
            byte[] buf = new byte[bytes.length];
            stream.read( buf );
            Assert.assertArrayEquals( bytes, buf );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
            Assert.fail( ex.getMessage() );
        }
    }

    @Test
    public void testReadFullyByteArray()
    {
        BufferedStream stream = new BufferedStream( byteStream );

        try
        {
            byte[] buf = new byte[bytes.length];
            stream.readFully( buf );
            Assert.assertArrayEquals( bytes, buf );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
            Assert.fail( ex.getMessage() );
        }
    }

    @Test
    public void testReadByteArrayIntInt()
    {
        BufferedStream stream = new BufferedStream( byteStream );

        try
        {
            byte[] buf = new byte[bytes.length];
            stream.read( buf, 0, buf.length );
            Assert.assertArrayEquals( bytes, buf );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
            Assert.fail( ex.getMessage() );
        }
    }

    @Test
    public void testReadFullyByteArrayIntInt()
    {
        BufferedStream stream = new BufferedStream( byteStream );

        try
        {
            byte[] buf = new byte[bytes.length];
            stream.readFully( buf, 0, buf.length );
            Assert.assertArrayEquals( bytes, buf );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
            Assert.fail( ex.getMessage() );
        }
    }

    @Test
    public void testSeek()
    {
        int off = ( int )( bytes.length / 3.0 );
        int len = ( int )( bytes.length * 2 / 3.0 );
        BufferedStream stream = new BufferedStream( byteStream, 5 );

        try
        {
            stream.seek( off );
            Assert.assertEquals( off, stream.getPosition() );
            byte b = stream.read();
            Assert.assertEquals( bytes[off], b );

            stream.seek( len );
            Assert.assertEquals( len, stream.getPosition() );
            Assert.assertEquals( bytes[len], stream.read() );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
            Assert.fail( ex.getMessage() );
        }
    }

    @Test
    public void testClose()
    {
        BufferedStream stream = new BufferedStream( byteStream );

        try
        {
            stream.close();
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
            Assert.fail( ex.getMessage() );
        }
    }

    @Test
    public void testGetPosition()
    {
        BufferedStream stream = new BufferedStream( byteStream );

        try
        {
            Assert.assertEquals( 0, stream.getPosition() );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
            Assert.fail( ex.getMessage() );
        }
    }

    @Test
    public void testGetLength()
    {
        BufferedStream stream = new BufferedStream( byteStream );

        try
        {
            Assert.assertEquals( bytes.length, stream.getLength() );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
            Assert.fail( ex.getMessage() );
        }
    }

    @Test
    public void testWriteData()
    {
        ByteArrayStream byteStream = new ByteArrayStream( 0 );
        BufferedStream stream = new BufferedStream( byteStream );

        try
        {
            int len = 0;

            try
            {
                int count = ( int )( ByteCache.DEFAULT_SIZE / bytes.length ) + 1;

                count = 1;
                Assert.assertEquals( 0, stream.getLength() );

                stream.write( ( byte )55 );
                stream.write( ( byte )55 );
                stream.write( ( byte )55 );
                stream.write( ( byte )55 );

                len += 4;

                Assert.assertEquals( 4, stream.getLength() );

                for( int i = 0; i < count; i++ )
                {
                    stream.write( bytes );
                    len += bytes.length;
                }

                Assert.assertEquals( len, stream.getLength() );
            }
            finally
            {
                stream.close();
            }
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
            Assert.fail( ex.getMessage() );
        }
    }
}
