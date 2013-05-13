package org.jutils.io;

import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;

/**
 * 
 */
public class ByteArrayStream implements IStream
{
    public static int DEFAULT_SIZE = 1024;

    /** Current position in the buffer where data will be read or written. */
    private int position;
    /** The buffer to be used for reading/writing. */
    private byte[] buffer;
    /** The used size of the current buffer. */
    private int bufferSize;
    /** The number of bytes by which the buffer size will increase as needed. */
    private int sizeIncrement;

    /***************************************************************************
     * 
     **************************************************************************/
    public ByteArrayStream()
    {
        this( new byte[DEFAULT_SIZE], 0 );
    }

    public ByteArrayStream( byte[] buf )
    {
        this( buf, buf.length );
    }

    public ByteArrayStream( int size )
    {
        this( new byte[size], size );
    }

    public ByteArrayStream( byte[] buf, int size )
    {
        this( buf, size, DEFAULT_SIZE );
    }

    public ByteArrayStream( byte[] buf, int size, int increment )
    {
        this( buf, size, increment, true );
    }

    public ByteArrayStream( byte[] buf, int size, int increment, boolean copy )
    {
        if( copy )
        {
            this.buffer = Arrays.copyOf( buf, size );
        }
        else
        {
            this.buffer = buf;
        }
        this.position = 0;
        this.bufferSize = size;
        this.sizeIncrement = increment;
    }

    /***************************************************************************
     * @param length
     **************************************************************************/
    private void ensureWrite( int length )
    {
        int nextPos = position + length;
        int diff = nextPos - buffer.length;
        int inc = sizeIncrement > diff ? sizeIncrement : diff;

        if( nextPos > buffer.length )
        {
            buffer = Arrays.copyOf( buffer, buffer.length + inc );
        }

        if( nextPos > bufferSize )
        {
            bufferSize = nextPos;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void close() throws IOException
    {
        ;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public long getPosition() throws IOException
    {
        return position;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public byte read() throws IOException
    {
        if( position >= bufferSize )
        {
            throw new EOFException( "Tried to read past end of stream" );
        }

        return buffer[position++];
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int read( byte[] buf ) throws IOException
    {
        return read( buf, 0, buf.length );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int read( byte[] buf, int off, int len ) throws IOException
    {
        if( position > bufferSize )
        {
            throw new EOFException( "Tried to read past end of stream" );
        }

        int bytesRead = ( int )getAvailableByteCount();

        if( len < bytesRead )
        {
            bytesRead = len;
        }

        System.arraycopy( buffer, position, buf, off, len );

        return bytesRead;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void readFully( byte[] buf ) throws IOException
    {
        readFully( buf, 0, buf.length );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void readFully( byte[] buf, int off, int len ) throws IOException
    {
        if( len > getAvailable() )
        {
            throw new IOException( "Cannot fill with " + len +
                " bytes as only " + getAvailableByteCount() +
                " bytes are available." );
        }

        System.arraycopy( buffer, position, buf, off, len );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void seek( long pos ) throws IOException
    {
        if( pos < 0 )
        {
            pos += position;
        }

        if( pos > Integer.MAX_VALUE )
        {
            throw new IOException( "Seek position to long for a byte buffer: " +
                pos + " > " + Integer.MAX_VALUE );
        }

        this.position = ( int )pos;
    }

    /***************************************************************************
     * Returns the number of bytes available for reading/writing in the current
     * buffer.
     * @return
     **************************************************************************/
    private long getAvailableByteCount()
    {
        return buffer.length - position;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void skip( long count ) throws IOException
    {
        seek( getPosition() + count );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public long getAvailable() throws IOException
    {
        return getLength() - getPosition();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public long getLength() throws IOException
    {
        return bufferSize;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( byte b ) throws IOException
    {
        ensureWrite( 1 );

        buffer[position++] = b;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( byte[] buf ) throws IOException
    {
        write( buf, 0, buf.length );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( byte[] buf, int off, int len ) throws IOException
    {
        ensureWrite( len );

        System.arraycopy( buf, off, buffer, position, len );

        position += len;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public byte[] toByteArray()
    {
        byte[] bytes = new byte[bufferSize];

        System.arraycopy( buffer, 0, bytes, 0, bufferSize );

        return bytes;
    }
}
