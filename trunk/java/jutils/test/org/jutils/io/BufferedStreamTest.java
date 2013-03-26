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
        BufferedStream stream = new BufferedStream( byteStream, 75 );

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

    @Test
    public void testWriteToDataStream2()
    {
        byte[] expected = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        byte[] buffer = new byte[expected.length];
        ByteArrayStream byteStream = new ByteArrayStream( buffer );
        BufferedStream stream = new BufferedStream( byteStream );

        try
        {
            try
            {
                stream.write( expected, 0, 4 );
                stream.write( expected[4] );
                stream.write( expected, 5, 6 );

                byte[] actual = new byte[expected.length];

                stream.seek( 0 );
                stream.readFully( actual );

                Assert.assertArrayEquals( expected, actual );
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

    @Test
    public void testWriteToDataStream()
    {
        byte[] buffer = new byte[100];
        ByteArrayStream byteStream = new ByteArrayStream( buffer );
        BufferedStream bufStream = new BufferedStream( byteStream );
        DataStream stream = new DataStream( bufStream );

        try
        {
            try
            {
                stream.writeInt( 4 );

                stream.write( ( byte )42 );

                Assert.assertEquals( 5, stream.getPosition() );

                stream.seek( 0 );

                Assert.assertEquals( 0, stream.getPosition() );

                stream.readInt();

                Assert.assertEquals( 4, stream.getPosition() );

                byte b = stream.read();

                Assert.assertEquals( 5, stream.getPosition() );

                Assert.assertEquals( 42, b );
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

    private static class MockObject
    {
        public int i = 8;
        public boolean b = true;
        public double d = 9.0;
        public float f = 4.5f;
        public char c = 'y';
        public long l = 42;

        @Override
        public boolean equals( Object obj )
        {
            if( obj != null )
            {
                if( obj == this )
                {
                    return true;
                }
                else
                {
                    if( obj instanceof MockObject )
                    {
                        MockObject that = ( MockObject )obj;
                        return i == that.i && b == that.b &&
                            Math.abs( d - that.d ) < 0.00001 &&
                            Math.abs( f - that.f ) < 0.00001f && c == that.c &&
                            l == that.l;
                    }
                    else
                    {
                        return false;
                    }
                }
            }

            return false;
        }

        @Override
        public String toString()
        {
            StringBuilder str = new StringBuilder();

            str.append( "i = " );
            str.append( i );

            str.append( ", b = " );
            str.append( b );

            str.append( ", d = " );
            str.append( d );

            str.append( ", f = " );
            str.append( f );

            str.append( ", c = " );
            str.append( c );

            str.append( ", l = " );
            str.append( l );

            return str.toString();
        }
    }

    private static class MockObjectSerializer implements
        IStdSerializer<MockObject, IDataStream>
    {
        @Override
        public MockObject read( IDataStream stream ) throws IOException,
            RuntimeFormatException
        {
            MockObject obj = new MockObject();

            obj.i = stream.readInt();
            obj.b = stream.readBoolean();
            // obj.d = stream.readDouble();
            // obj.f = stream.readFloat();
            // obj.c = ( char )stream.read();
            // obj.l = stream.readLong();

            return obj;
        }

        @Override
        public void write( MockObject item, IDataStream stream )
            throws IOException
        {
            stream.writeInt( item.i );
            // stream.writeBoolean( item.b );
            stream.write( ( byte )1 );
            // stream.writeDouble( item.d );
            // stream.writeFloat( item.f );
            // stream.write( ( byte )item.c );
            // stream.writeLong( item.l );
        }
    }
}
