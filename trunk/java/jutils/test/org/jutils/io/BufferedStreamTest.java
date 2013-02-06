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
            Assert.assertEquals( stream.getPosition(), off );
            Assert.assertEquals( stream.read(), bytes[off] );

            stream.seek( len );
            Assert.assertEquals( stream.getPosition(), len );
            Assert.assertEquals( stream.read(), bytes[len] );
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
            Assert.assertEquals( stream.getPosition(), 0 );
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
            Assert.assertEquals( stream.getLength(), bytes.length );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
            Assert.fail( ex.getMessage() );
        }
    }

}
