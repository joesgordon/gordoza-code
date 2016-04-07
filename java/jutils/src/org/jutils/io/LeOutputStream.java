package org.jutils.io;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/*******************************************************************************
 * 
 ******************************************************************************/
public class LeOutputStream extends FilterOutputStream implements DataOutput
{
    /**  */
    private final byte [] bytes;
    /**  */
    private final ByteBuffer buffer;

    /***************************************************************************
     * @param out
     **************************************************************************/
    public LeOutputStream( OutputStream out )
    {
        super( out );

        this.bytes = new byte[8];
        this.buffer = ByteBuffer.wrap( bytes );

        buffer.order( ByteOrder.LITTLE_ENDIAN );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeBoolean( boolean v ) throws IOException
    {
        super.write( v ? 1 : 0 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeByte( int v ) throws IOException
    {
        super.write( v );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeShort( int v ) throws IOException
    {
        buffer.rewind();
        buffer.putShort( ( short )v );

        super.write( bytes, 0, 2 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeInt( int v ) throws IOException
    {
        buffer.rewind();
        buffer.putInt( v );

        super.write( bytes, 0, 4 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeLong( long v ) throws IOException
    {
        buffer.rewind();
        buffer.putLong( v );
        buffer.get( bytes );

        super.write( bytes, 0, 8 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeFloat( float v ) throws IOException
    {
        buffer.rewind();
        buffer.putFloat( v );

        super.write( bytes, 0, 4 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeDouble( double v ) throws IOException
    {
        buffer.rewind();
        buffer.putDouble( v );

        super.write( bytes, 0, 8 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeBytes( String s ) throws IOException
    {
        super.write( s.getBytes() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeChar( int v ) throws IOException
    {
        throw new RuntimeException( "Function not implemented" );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeChars( String s ) throws IOException
    {
        throw new RuntimeException( "Function not implemented" );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void writeUTF( String s ) throws IOException
    {
        throw new RuntimeException( "Function not implemented" );
    }
}
