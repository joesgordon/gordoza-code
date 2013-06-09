package org.jutils.io;

import java.io.EOFException;
import java.io.IOException;

/*******************************************************************************
 * {@code BufferedStream} is an {@link IStream} that buffers another
 * {@code IStream}.
 ******************************************************************************/
public class BufferedStream implements IStream
{
    // -------------------------------------------------------------------------
    // Immutable data members.
    // -------------------------------------------------------------------------

    /** The stream to do all actual reads/writes */
    private final IStream stream;
    /** The buffer to be used */
    private final ByteCache buffer;

    // -------------------------------------------------------------------------
    // Mutable data members.
    // -------------------------------------------------------------------------

    /**
     * Flag that specifies whether the buffer should be written ({@code true})
     * on next flush of the streams or not ({@code false}).
     */
    private boolean writeOnNextFlush = false;
    /** The position in this stream of the next read/write. */
    private long position;
    /**  */
    private long streamLen;

    /***************************************************************************
     * Buffers the provided stream with a buffer of
     * {@link ByteCache#DEFAULT_SIZE default size}.
     * @param stream the underlying stream to be buffered.
     **************************************************************************/
    public BufferedStream( IStream stream )
    {
        this( stream, ByteCache.DEFAULT_SIZE );
    }

    /***************************************************************************
     * Buffers the provided stream with a buffer of the provided size.
     * @param stream the underlying stream to be buffered.
     * @param bufSize the size of the buffer.
     **************************************************************************/
    public BufferedStream( IStream stream, int bufSize )
    {
        this.position = 0;
        this.stream = stream;
        this.buffer = new ByteCache( bufSize );
        this.streamLen = -1;
    }

    /***************************************************************************
     * Flushes the buffer to the underlying stream if there is data to be
     * written.
     * @throws IOException If an I/O error occurs during write.
     **************************************************************************/
    public void flush() throws IOException
    {
        if( writeOnNextFlush )
        {
            boolean updateLength = false;

            if( buffer.isReadCached( streamLen ) )
            {
                updateLength = true;
            }

            buffer.writeToStream( stream );
            writeOnNextFlush = false;

            if( updateLength )
            {
                streamLen = getLength();
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public byte read() throws IOException
    {
        byte b;

        // printDebug( "read-pre" );

        if( getPosition() >= getLength() )
        {
            throw new EOFException( "Tried to read past end of stream" );
        }

        ensureReadCache();

        b = buffer.read();

        position++;

        // printDebug( "read-post" );

        return b;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int read( byte[] buf ) throws IOException
    {
        int len = read( buf, 0, buf.length );

        position += len;

        return len;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void readFully( byte[] buf ) throws IOException
    {
        readFully( buf, 0, buf.length );

        position += buf.length;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int read( byte[] buf, int off, int len ) throws IOException
    {
        int bytesRead = 0;
        int bytesRemaining = len;

        while( bytesRemaining > 0 )
        {
            // printDebug( "read-pre" );

            ensureReadCache();

            if( getPosition() >= getLength() )
            {
                throw new EOFException( "Tried to read past end of stream" );
            }

            // -----------------------------------------------------------------
            // Determine how many bytes can be read out of this cache.
            // -----------------------------------------------------------------
            bytesRead = buffer.remainingWrite();

            if( bytesRemaining < bytesRead )
            {
                bytesRead = bytesRemaining;
            }

            // -----------------------------------------------------------------
            // Copy these bytes into the client buffer.
            // -----------------------------------------------------------------
            buffer.read( buf, off, bytesRead );

            bytesRemaining -= bytesRead;
            off += bytesRead;
            position += bytesRead;

            // printDebug( "read-post: " + bytesRemaining );
        }

        return len;
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

        if( pos < 0 )
        {
            position += pos;
        }
        else
        {
            position = pos;
        }

        // printDebug( "seek-post" );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void close() throws IOException
    {
        flush();
        stream.close();
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
    public long getPosition() throws IOException
    {
        return position;
    }

    /***************************************************************************
     * Returns the virtual length of this stream (i.e. the returned value
     * includes any bytes written past the end of the underlying stream which
     * have not yet been written to disk).
     * @return the virtual length of this stream.
     **************************************************************************/
    @Override
    public long getLength() throws IOException
    {
        long len = buffer.getPosition() + buffer.getReadLength();

        if( streamLen < 0 )
        {
            streamLen = stream.getLength();
        }

        return Math.max( len, streamLen );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( byte b ) throws IOException
    {
        // ---------------------------------------------------------------------
        // If cached, write and set flush-write flag.
        // ---------------------------------------------------------------------
        if( buffer.isWriteCached( position ) )
        {
            buffer.write( b );
            position++;
            writeOnNextFlush = true;
        }
        else
        {
            // -----------------------------------------------------------------
            // Otherwise, write directly to the stream and cache afterwards.
            // -----------------------------------------------------------------
            stream.seek( position );
            stream.write( b );
            position++;
            buffer.readFromStream( stream );
        }
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
        if( buffer.isWriteCached( position ) )
        {
            if( len > buffer.remainingWrite() )
            {
                int toWrite = len - buffer.remainingWrite();
                int nextOff = off + buffer.remainingWrite();

                if( writeOnNextFlush )
                {
                    buffer.write( buf, off, buffer.remainingWrite() );
                    flush();
                }

                if( toWrite > 0 )
                {
                    stream.write( buf, off, nextOff );
                }

                position += len;

                loadBufferFromFile( position );
            }
            else
            {
                buffer.write( buf, off, len );
                writeOnNextFlush = true;

                position += len;
            }
        }
        else
        {
            // -----------------------------------------------------------------
            // Otherwise, write directly to the stream and cache afterwards.
            // -----------------------------------------------------------------
            stream.seek( position );
            stream.write( buf, off, len );
            position += len;
            buffer.readFromStream( stream );
        }
    }

    /***************************************************************************
     * Write a little info about the state of the stream along with the provided
     * message.
     * @param msg the message to be written.
     **************************************************************************/
    protected void printDebug( String msg )
    {
        System.out.print( "---------------------------- " + msg );
        System.out.println( "----------------------------" );
        buffer.printDebug();
        System.out.println();
    }

    /***************************************************************************
     * Flushes and then loads the buffer starting at the provided position into
     * the stream.
     * @param pos the position into the underlying stream at which the buffer
     * will be loaded.
     * @throws IOException If an I/O error occurs.
     **************************************************************************/
    private void loadBufferFromFile( long pos ) throws IOException
    {
        // printDebug( "pre-load" );

        flush();

        buffer.readFromStream( stream, pos );

        // printDebug( "post-load" );
    }

    /***************************************************************************
     * Ensures that the current position is cached to be read.
     * @throws IOException If an I/O error occurs.
     **************************************************************************/
    private void ensureReadCache() throws IOException
    {
        long pos = getPosition();

        if( !buffer.isReadCached( pos ) )
        {
            loadBufferFromFile( pos );
        }
        else
        {
            buffer.setPosition( pos );
        }
    }
}