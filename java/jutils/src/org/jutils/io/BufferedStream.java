package org.jutils.io;

import java.io.EOFException;
import java.io.IOException;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BufferedStream implements IStream
{
    /** Default size of 8 MB. */
    public static final int DEFAULT_SIZE = 0x800000;

    /** The stream to do all actual reads/writes */
    private final IStream stream;
    /** The buffer to be used */
    private final byte[] buffer;
    /** The position of the beginning of the buffer in the file. */
    private long streamPosition;
    /** The position within the buffer for the next read/write. */
    private int bufferIndex;
    /** The number of the valid bytes in the current buffer. */
    private int bufferLength;
    /**
     * {@code true} if the buffer is loaded with the current position,
     * {@code false}, otherwise.
     */
    private boolean empty;
    /**  */
    private boolean writeOnNextFlush = false;

    /***************************************************************************
     * @param stream
     **************************************************************************/
    public BufferedStream( IStream stream )
    {
        // Default size of 8 MB
        this( stream, DEFAULT_SIZE );
    }

    /***************************************************************************
     * @param stream
     * @param bufSize
     **************************************************************************/
    public BufferedStream( IStream stream, int bufSize )
    {
        this.stream = stream;
        this.buffer = new byte[bufSize];
        this.bufferLength = 0;
        this.bufferIndex = 0;
        this.streamPosition = 0;
        this.empty = true;
    }

    /***************************************************************************
     * @param pos
     * @return
     * @throws IOException
     **************************************************************************/
    private boolean isCached( long pos )
    {
        return ( pos >= streamPosition && pos < ( streamPosition + buffer.length ) ) ||
            empty;
    }

    /***************************************************************************
     * @param pos
     * @throws IOException
     **************************************************************************/
    private void loadBufferFromFile( long pos ) throws IOException
    {
        // printDebug( "pre-load" );

        if( writeOnNextFlush )
        {
            writeBuffer();
        }

        streamPosition = pos;
        long len = stream.getLength() - pos;

        if( len <= 0 )
        {
            throw new EOFException();
        }

        if( len > buffer.length )
        {
            bufferLength = buffer.length;
        }
        else
        {
            bufferLength = ( int )len;
        }

        stream.seek( pos );
        stream.readFully( buffer, 0, bufferLength );
        empty = false;
        // printDebug( "post-load" );
    }

    private void writeBuffer() throws IOException
    {
        // printDebug( "pre-write" );
        stream.write( buffer, 0, bufferLength );
        // printDebug( "post-write" );
    }

    /***************************************************************************
     * @throws IOException
     **************************************************************************/
    private void ensureCache() throws IOException
    {
        if( bufferIndex >= bufferLength || empty )
        {
            long pos = getPosition();

            loadBufferFromFile( pos );

            bufferIndex = ( int )( pos - streamPosition );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public byte read() throws IOException
    {
        // printDebug( "read-pre" );

        if( getPosition() >= getLength() )
        {
            throw new EOFException( "Tried to read past end of stream" );
        }

        ensureCache();

        // printDebug( "read-post" );

        return buffer[bufferIndex++];
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
    public void readFully( byte[] buf ) throws IOException
    {
        readFully( buf, 0, buf.length );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int read( byte[] buf, int off, int len ) throws IOException
    {
        int readLen = 0;
        int bytesRemaining = len;

        while( bytesRemaining > 0 )
        {
            // printDebug( "read-pre" );
            ensureCache();

            // Determine how many bytes can be read out of this cache.
            readLen = bytesRemaining;
            if( readLen > ( bufferLength - bufferIndex ) )
            {
                readLen = bufferLength - bufferIndex;
            }

            // Copy these bytes into the client buffer.
            System.arraycopy( buffer, bufferIndex, buf, off, readLen );

            bytesRemaining -= readLen;
            bufferIndex += readLen;
            off += readLen;

            // printDebug( "read-post: " + bytesRemaining );
        }

        return len - bytesRemaining;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void readFully( byte[] buf, int off, int len ) throws IOException
    {
        int bytesRead = 0;

        while( bytesRead < len )
        {
            bytesRead += read( buf, off + bytesRead, len - bytesRead );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void seek( long pos ) throws IOException
    {
        // printDebug( "seek-pre" );

        // ---------------------------------------------------------------------
        // Seek back if the position requested is less than zero.
        // ---------------------------------------------------------------------
        if( pos < 0 )
        {
            pos += streamPosition + bufferIndex;
        }

        // ---------------------------------------------------------------------
        // If the position is not cached or the cache is empty, just set the
        // offset positions so that ensureCache will process correctly upon the
        // next read/write. Else, just set the buffer index.
        // ---------------------------------------------------------------------
        if( !isCached( pos ) || empty )
        {
            streamPosition = pos;
            bufferIndex = 0;
            empty = true;
        }
        else
        {
            bufferIndex = ( int )( pos - streamPosition );
            empty = false;
        }

        // printDebug( "seek-post" );
    }

    /***************************************************************************
     * @param msg
     **************************************************************************/
    @SuppressWarnings( "unused")
    private void printDebug( String msg )
    {
        System.out.println( "---------------------------- " + msg +
            "----------------------------" );
        System.out.println( "  bufPos: " + streamPosition );
        System.out.println( "bufIndex: " + bufferIndex );
        System.out.println( "  bufLen: " + bufferLength );
        System.out.println( "   empty: " + empty );
        System.out.println();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void close() throws IOException
    {
        if( writeOnNextFlush )
        {
            writeBuffer();
        }

        stream.close();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public long getPosition() throws IOException
    {
        return streamPosition + bufferIndex;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public long getLength() throws IOException
    {
        return Math.max( stream.getLength(), streamPosition + bufferLength );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( byte b ) throws IOException
    {
        ensureCache();

        writeOnNextFlush = true;

        buffer[bufferIndex++] = b;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( byte[] buf ) throws IOException
    {
        // TODO Auto-generated method stub
        writeOnNextFlush = true;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( byte[] buf, int off, int len ) throws IOException
    {
        // TODO Auto-generated method stub

        writeOnNextFlush = true;
    }
}
