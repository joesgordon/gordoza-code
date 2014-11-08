package org.jutils.io;

import java.io.*;

import org.junit.*;
import org.jutils.IconConstants;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BufferedStreamTest
{
    /**  */
    private ByteArrayStream byteStream;
    /**  */
    private byte [] bytes;

    /***************************************************************************
     * @return
     * @throws IOException
     **************************************************************************/
    private byte [] loadTestBytes() throws IOException
    {
        try( InputStream stream = IconConstants.loader.loader.getUrl(
            IconConstants.ATOMIC_32 ).openStream() )
        {
            byte [] bytes = new byte[stream.available()];

            stream.read( bytes );
            stream.close();

            return bytes;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
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

    /***************************************************************************
     * 
     **************************************************************************/
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

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testRead()
    {
        try( BufferedStream stream = new BufferedStream( byteStream ) )
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

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testReadByteArray()
    {
        try( BufferedStream stream = new BufferedStream( byteStream, 75 ) )
        {
            byte [] buf = new byte[bytes.length];
            stream.read( buf );
            Assert.assertArrayEquals( bytes, buf );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
            Assert.fail( ex.getMessage() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testReadFullyByteArray()
    {
        try( BufferedStream stream = new BufferedStream( byteStream ) )
        {
            byte [] buf = new byte[bytes.length];
            stream.readFully( buf );
            Assert.assertArrayEquals( bytes, buf );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
            Assert.fail( ex.getMessage() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testReadFullyGreaterThanAvailable()
    {
        try( BufferedStream stream = new BufferedStream( byteStream ) )
        {
            byte [] buf = new byte[bytes.length + 10];
            stream.readFully( buf );
            Assert.fail( "An EOF Exception should have been thrown." );
        }
        catch( EOFException ex )
        {
            Assert.assertTrue( true );
            return;
        }
        catch( IOException ex )
        {
            Assert.fail( "An EOF Exception should have been thrown instead of an I/O Exception." );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testReadByteArrayIntInt()
    {
        try( BufferedStream stream = new BufferedStream( byteStream ) )
        {
            byte [] buf = new byte[bytes.length];
            stream.read( buf, 0, buf.length );
            Assert.assertArrayEquals( bytes, buf );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
            Assert.fail( ex.getMessage() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testReadFullyByteArrayIntInt()
    {
        try( BufferedStream stream = new BufferedStream( byteStream ) )
        {
            byte [] buf = new byte[bytes.length];
            stream.readFully( buf, 0, buf.length );
            Assert.assertArrayEquals( bytes, buf );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
            Assert.fail( ex.getMessage() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testSeek()
    {
        int off = ( int )( bytes.length / 3.0 );
        int len = ( int )( bytes.length * 2 / 3.0 );

        try( BufferedStream stream = new BufferedStream( byteStream, 5 ) )
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

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testClose()
    {
        try( BufferedStream stream = new BufferedStream( byteStream ) )
        {
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
            Assert.fail( ex.getMessage() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testGetPosition()
    {
        try( BufferedStream stream = new BufferedStream( byteStream ) )
        {
            Assert.assertEquals( 0, stream.getPosition() );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
            Assert.fail( ex.getMessage() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testGetLength()
    {
        try( BufferedStream stream = new BufferedStream( byteStream ) )
        {
            Assert.assertEquals( bytes.length, stream.getLength() );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
            Assert.fail( ex.getMessage() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testWriteData()
    {
        try( ByteArrayStream byteStream = new ByteArrayStream( 0 );
             BufferedStream stream = new BufferedStream( byteStream ) )
        {
            int len = 0;

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
        catch( IOException ex )
        {
            ex.printStackTrace();
            Assert.fail( ex.getMessage() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testWriteToDataStream2()
    {
        byte [] expected = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        byte [] buffer = new byte[expected.length];

        try( ByteArrayStream byteStream = new ByteArrayStream( buffer );
             BufferedStream stream = new BufferedStream( byteStream ) )
        {
            stream.write( expected, 0, 4 );
            stream.write( expected[4] );
            stream.write( expected, 5, 6 );

            byte [] actual = new byte[expected.length];

            stream.seek( 0 );
            stream.readFully( actual );

            Assert.assertArrayEquals( expected, actual );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
            Assert.fail( ex.getMessage() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testWriteToDataStream3()
    {
        byte [] expected = new byte[7];
        byte [] actual = new byte[expected.length];
        int WRITE_COUNT = 235;

        for( int i = 0; i < expected.length; i++ )
        {
            expected[i] = ( byte )( i );
        }

        try
        {
            File file = File.createTempFile( getClass().getSimpleName() + "_",
                ".bin" );
            file.deleteOnExit();

            try( FileStream fstream = new FileStream( file );
                 BufferedStream stream = new BufferedStream( fstream, 16 ) )
            {
                for( int i = 0; i < WRITE_COUNT; i++ )
                {
                    stream.write( expected );
                }

                stream.write( ( byte )0 );
                stream.write( ( byte )10 );
                stream.write( ( byte )20 );
                stream.write( ( byte )30 );
            }

            Assert.assertEquals( WRITE_COUNT * expected.length + 4,
                file.length() );

            try( FileStream fstream = new FileStream( file, true );
                 BufferedStream stream = new BufferedStream( fstream, 16 ) )
            {
                stream.seek( 0 );
                for( int i = 0; i < WRITE_COUNT; i++ )
                {
                    stream.readFully( actual );

                    Assert.assertArrayEquals( expected, actual );
                }
            }

        }
        catch( IOException ex )
        {
            ex.printStackTrace();
            Assert.fail( ex.getMessage() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testWriteToDataStream()
    {
        byte [] buffer = new byte[100];

        try( ByteArrayStream byteStream = new ByteArrayStream( buffer );
             BufferedStream bufStream = new BufferedStream( byteStream );
             DataStream stream = new DataStream( bufStream ) )
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
        catch( IOException ex )
        {
            ex.printStackTrace();
            Assert.fail( ex.getMessage() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Test
    public void testWriteMockDataToDataStream()
    {
        byte [] buffer = new byte[100];

        MockObjectSerializer serializer = new MockObjectSerializer();
        MockObject expected = new MockObject();
        MockObject actual;

        try( ByteArrayStream byteStream = new ByteArrayStream( buffer );
             BufferedStream bufStream = new BufferedStream( byteStream );
             DataStream stream = new DataStream( bufStream ) )
        {
            serializer.write( expected, stream );

            stream.seek( 0 );

            actual = serializer.read( stream );

            Assert.assertEquals( expected, actual );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
            Assert.fail( ex.getMessage() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
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

    /***************************************************************************
     * 
     **************************************************************************/
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
