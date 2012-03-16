package org.jutils.io;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class LeInputStream extends CountingInputStream implements DataInput
{
    private ByteBuffer buffer;
    private byte[] bytes;

    public LeInputStream( InputStream stream )
    {
        super( stream );
        bytes = new byte[8];
        buffer = ByteBuffer.allocate( 256 );
        buffer.order( ByteOrder.LITTLE_ENDIAN );
    }

    @Override
    public boolean readBoolean() throws IOException
    {
        return readByte() != 0;
    }

    @Override
    public byte readByte() throws IOException
    {
        int b = super.read();

        if( b < 0 )
        {
            throw new IOException( "Cannot read past end of file." );
        }

        if( b > 127 )
        {
            b -= 256;
        }

        return ( byte )b;
    }

    @Override
    public char readChar() throws IOException
    {
        byte b = readByte();

        if( b < 0 )
        {
            b += 256;
        }

        return ( char )b;
    }

    @Override
    public double readDouble() throws IOException
    {
        int bytesRead = super.read( bytes );

        if( bytesRead != 8 )
        {
            throw new EOFException(
                "End-of-File reached while trying to read a double. Only " +
                    bytesRead + " bytes read." );
        }

        buffer.clear();
        buffer.put( bytes );
        buffer.position( 0 );

        return buffer.getDouble();
    }

    @Override
    public float readFloat() throws IOException
    {
        int bytesRead = super.read( bytes, 0, 4 );

        if( bytesRead != 4 )
        {
            throw new EOFException(
                "End-of-File reached while trying to read a float. Only " +
                    bytesRead + " bytes read." );
        }

        buffer.clear();
        buffer.put( bytes );
        buffer.position( 0 );

        return buffer.getFloat();
    }

    @Override
    public void readFully( byte[] b ) throws IOException
    {
        int bytesRead = super.read( b );

        if( b.length != bytesRead )
        {
            throw new EOFException(
                "End-of-File reached while trying to read " + b.length +
                    " bytes. Only " + bytesRead + " bytes read." );
        }
    }

    @Override
    public void readFully( byte[] b, int off, int len ) throws IOException
    {
        int bytesRead = super.read( b, off, len );

        if( len != bytesRead )
        {
            throw new EOFException(
                "End-of-File reached while trying to read " + b.length +
                    " bytes. Only " + bytesRead + " bytes read." );
        }
    }

    @Override
    public int readInt() throws IOException
    {
        int bytesRead = super.read( bytes, 0, 4 );

        if( bytesRead != 4 )
        {
            throw new EOFException(
                "End-of-File reached while trying to read a int. Only " +
                    bytesRead + " bytes read." );
        }

        buffer.clear();
        buffer.put( bytes );
        buffer.position( 0 );

        return buffer.getInt();
    }

    @Override
    public String readLine() throws IOException
    {
        throw new RuntimeException( "Not Implemented" );
    }

    @Override
    public long readLong() throws IOException
    {
        int bytesRead = super.read( bytes );

        if( bytesRead != 8 )
        {
            throw new EOFException(
                "End-of-File reached while trying to read a long. Only " +
                    bytesRead + " bytes read." );
        }

        buffer.clear();
        buffer.put( bytes );
        buffer.position( 0 );

        return buffer.getLong();
    }

    @Override
    public short readShort() throws IOException
    {
        int bytesRead = super.read( bytes, 0, 2 );

        if( bytesRead != 2 )
        {
            throw new EOFException(
                "End-of-File reached while trying to read a short. Only " +
                    bytesRead + " bytes read." );
        }

        buffer.clear();
        buffer.put( bytes );
        buffer.position( 0 );

        return buffer.getShort();
    }

    @Override
    public String readUTF() throws IOException
    {
        throw new RuntimeException( "Not Implemented" );
    }

    @Override
    public int readUnsignedByte() throws IOException
    {
        int b = readByte();

        if( b < 0 )
        {
            b += 256;
        }

        return b;
    }

    @Override
    public int readUnsignedShort() throws IOException
    {
        int bytesRead = super.read( bytes );

        if( bytesRead != 2 )
        {
            throw new EOFException(
                "End-of-File reached while trying to read a short. Only " +
                    bytesRead + " bytes read." );
        }

        buffer.clear();
        buffer.put( bytes );
        buffer.position( 0 );

        int s = buffer.getShort();

        if( s < 0 )
        {
            s += Short.MAX_VALUE;
        }

        return s;
    }

    @Override
    public int skipBytes( int n ) throws IOException
    {
        long bytes = super.skip( n );

        return ( int )bytes;
    }
}
