package org.jutils.io;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class LeOutputStream extends FilterOutputStream implements DataOutput
{
    private ByteBuffer buffer;
    private byte [] bytes;

    public LeOutputStream( OutputStream out )
    {
        super( out );
        bytes = new byte[8];
        buffer = ByteBuffer.allocate( 256 );
        buffer.order( ByteOrder.LITTLE_ENDIAN );
    }

    @Override
    public void writeBoolean( boolean v ) throws IOException
    {
        super.write( v ? 1 : 0 );
    }

    @Override
    public void writeByte( int v ) throws IOException
    {
        super.write( v );
    }

    @Override
    public void writeShort( int v ) throws IOException
    {
        buffer.clear();
        buffer.putShort( ( short )v );
        buffer.rewind();
        buffer.get( bytes );
        super.write( bytes, 0, 2 );
    }

    @Override
    public void writeChar( int v ) throws IOException
    {
        throw new RuntimeException( "Function not implemented" );
    }

    @Override
    public void writeInt( int v ) throws IOException
    {
        buffer.clear();
        buffer.putInt( v );
        buffer.rewind();
        buffer.get( bytes );
        Byte [] array = new Byte[4];

        for( int i = 0; i < array.length; i++ )
        {
            array[i] = bytes[i];
        }

        super.write( bytes, 0, 4 );
    }

    @Override
    public void writeLong( long v ) throws IOException
    {
        buffer.clear();
        buffer.putLong( v );
        buffer.rewind();
        buffer.get( bytes );
        super.write( bytes, 0, 8 );
    }

    @Override
    public void writeFloat( float v ) throws IOException
    {
        buffer.clear();
        buffer.putFloat( v );
        buffer.rewind();
        buffer.get( bytes );
        super.write( bytes, 0, 4 );
    }

    @Override
    public void writeDouble( double v ) throws IOException
    {
        buffer.clear();
        buffer.putDouble( v );
        buffer.rewind();
        buffer.get( bytes );
        super.write( bytes, 0, 8 );
    }

    @Override
    public void writeBytes( String s ) throws IOException
    {
        super.write( s.getBytes() );
    }

    @Override
    public void writeChars( String s ) throws IOException
    {
        throw new RuntimeException( "Function not implemented" );
    }

    @Override
    public void writeUTF( String s ) throws IOException
    {
        throw new RuntimeException( "Function not implemented" );
    }
}
