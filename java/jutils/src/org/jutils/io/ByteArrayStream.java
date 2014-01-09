package org.jutils.io;

import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;

/*******************************************************************************
 * Implements an {@link IStream} in memory backed by a {@code byte []}.
 ******************************************************************************/
public class ByteArrayStream implements IStream
{
    /** The default size of the buffer. */
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
     * Creates a new stream backed by {@link #DEFAULT_SIZE} bytes. The stream
     * will start with a length of 0.
     **************************************************************************/
    public ByteArrayStream()
    {
        this( DEFAULT_SIZE );
    }

    /***************************************************************************
     * Creates a new stream backed by the specified number of bytes. The stream
     * will start with a length of 0.
     * @param size the initial size of the buffer to back this buffer.
     **************************************************************************/
    public ByteArrayStream( int size )
    {
        this( new byte[size], size );
    }

    /***************************************************************************
     * Creates a new stream backed by a copy of the provided buffer with a
     * length of the size of the provided buffer.
     * @param buf the initial buffer for the stream.
     **************************************************************************/
    public ByteArrayStream( byte[] buf )
    {
        this( buf, buf.length );
    }

    /***************************************************************************
     * Creates a new stream backed by a copy of the provided buffer with a
     * length as specified.
     * @param buf the initial buffer for the stream.
     * @param length the initial length of the stream.
     **************************************************************************/
    public ByteArrayStream( byte[] buf, int length )
    {
        this( buf, length, DEFAULT_SIZE );
    }

    /***************************************************************************
     * Creates a new stream backed by a copy of the provided buffer with a
     * length as specified that will grow with the provided increment.
     * @param buf the initial buffer for the stream.
     * @param length the initial length of the stream.
     * @param increment the growth increment for writes that occur past the end
     * of the buffer.
     **************************************************************************/
    public ByteArrayStream( byte[] buf, int length, int increment )
    {
        this( buf, length, increment, true );
    }

    /***************************************************************************
     * Creates a new stream backed by the provided buffer (optionally a copy)
     * with a length as specified that will grow with the provided increment.
     * @param buf the initial buffer for the stream.
     * @param length the initial length of the stream.
     * @param increment the growth increment for writes that occur past the end
     * of the buffer.
     * @param copy copies the provided buffer if {@code true}, simply uses the
     * buffer until it grows otherwise.
     **************************************************************************/
    public ByteArrayStream( byte[] buf, int length, int increment, boolean copy )
    {
        if( copy )
        {
            buf = Arrays.copyOf( buf, length );
        }

        this.buffer = buf;
        this.position = 0;
        this.bufferSize = length;
        this.sizeIncrement = increment;
    }

    /***************************************************************************
     * Ensures a write of the provided number of bytes at the current position
     * will succeed by growing the {@link #buffer} if needed.
     * @param length the number of bytes to be written at the current position.
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
     * Returns the number of bytes available for reading/writing in the current
     * buffer.
     **************************************************************************/
    private long getAvailableByteCount()
    {
        return buffer.length - position;
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
     * Returns a copy of the current buffer trimmed to the length of this
     * stream.
     **************************************************************************/
    public byte[] toByteArray()
    {
        byte[] bytes = new byte[bufferSize];

        System.arraycopy( buffer, 0, bytes, 0, bufferSize );

        return bytes;
    }
}
