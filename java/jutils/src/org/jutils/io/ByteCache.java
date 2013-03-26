package org.jutils.io;

import java.io.IOException;
import java.util.Arrays;

/***************************************************************************
 * Designed to assist with buffering of {@link IStream}s.
 **************************************************************************/
public class ByteCache
{
    /** Default size of 8 MB. */
    public static final int DEFAULT_SIZE = 0x800000;

    /** The actual cache of bytes to be used. */
    private final byte[] cache;

    /** The position this cache represents in a stream. */
    private long position;
    /** The used length of this cache. */
    private int cacheLen;
    /** The current read/write index into this cache. */
    private int index;

    /***************************************************************************
     * Creates a new cache of with a buffer of {@link ByteCache#DEFAULT_SIZE}.
     **************************************************************************/
    public ByteCache()
    {
        this( DEFAULT_SIZE );
    }

    /***************************************************************************
     * Creates a new cache with the provided size.
     * @param size the size of this cache.
     **************************************************************************/
    public ByteCache( int size )
    {
        this( new byte[size] );
    }

    /***************************************************************************
     * @param bytes
     **************************************************************************/
    public ByteCache( byte[] bytes )
    {
        this.cache = bytes;
        this.position = -1;
    }

    /***************************************************************************
     * Prints debug information about this cache to the console.
     **************************************************************************/
    protected void printDebug()
    {
        System.out.println( "  bufPos: " + position );
        System.out.println( "bufIndex: " + index );
        System.out.println( "  bufLen: " + cacheLen );
        System.out.println( " bufSize: " + cache.length );
        System.out.println( "   empty: " + ( position < 0 ) );
    }

    /***************************************************************************
     * Returns the number of bytes past the last read/write index that are
     * available for reading in this cache.
     **************************************************************************/
    public int remainingRead()
    {
        return cacheLen - index;
    }

    /***************************************************************************
     * Returns the number of bytes past the last read/write index that are
     * available for writing in this cache.
     **************************************************************************/
    public int remainingWrite()
    {
        return cache.length - index;
    }

    /***************************************************************************
     * Tests the provided position to ascertain if it is contained within this
     * cache and available for reading.
     * @param pos the position to be tested.
     * @return {@code true} if the position is contained within this cache and
     * available for reading, {@code false} otherwise.
     **************************************************************************/
    public boolean isReadCached( long pos )
    {
        return position > -1 && pos >= position && pos < position + cacheLen;
    }

    /***************************************************************************
     * Tests the provided position to ascertain if it is contained within this
     * cache and available for writing.
     * @param pos the position to be tested.
     * @return {@code true} if the position is contained within this cache and
     * available for writing, {@code false} otherwise.
     **************************************************************************/
    public boolean isWriteCached( long pos )
    {
        return position > -1 && pos >= position &&
            pos < position + cache.length;
    }

    /***************************************************************************
     * Reads the next block from the provided stream up to the cache size, or to
     * the end of the stream (lesser of the two).
     * @param stream the stream to be read.
     * @throws IOException If an I/O error occurs.
     **************************************************************************/
    public void readFromStream( IStream stream ) throws IOException
    {
        long pos = stream.getPosition();

        readFromStream( stream, pos );
    }

    /***************************************************************************
     * Reads the next block from the provided stream starting at the provided
     * position up to the cache size, or to the end of the stream (lesser of the
     * two).
     * @param stream the stream to be read.
     * @param pos the position in the stream to start reading.
     * @throws IOException If an I/O error occurs.
     **************************************************************************/
    public void readFromStream( IStream stream, long pos ) throws IOException
    {
        position = pos;

        if( pos < stream.getLength() )
        {
            long avail;

            stream.seek( pos );
            avail = stream.getAvailable();

            cacheLen = cache.length;
            if( avail < cacheLen )
            {
                cacheLen = ( int )avail;
            }

            stream.readFully( cache, 0, cacheLen );
        }
        else
        {
            cacheLen = 0;
        }

        index = 0;

        Arrays.fill( cache, cacheLen, cache.length, ( byte )0 );
    }

    /***************************************************************************
     * Writes the current cache to the provided stream at the location from
     * which it was read.
     * @param stream the stream to which the cache will be written.
     * @throws IOException If an I/O error occurs.
     **************************************************************************/
    public void writeToStream( IStream stream ) throws IOException
    {
        stream.seek( position );
        stream.write( cache, 0, cacheLen );
    }

    /***************************************************************************
     * Reads the next byte from the cache.
     * @return the next byte from the cache.
     * @throws IndexOutOfBoundsException If the position is past the end of the
     * cache.
     **************************************************************************/
    public byte read() throws IndexOutOfBoundsException
    {
        if( index >= cacheLen )
        {
            throw new IndexOutOfBoundsException( "Index (" + index +
                ") past/equal to cache length (" + cacheLen + ")" );
        }

        return cache[index++];
    }

    /***************************************************************************
     * Reads the next bytes from the cache to the provided buffer at the
     * provided offset for the provided length.
     * @param buf the buffer to which the data will be copied.
     * @param off the offset into the buffer at which the data will be copied.
     * @param len the length of the data to copy.
     * @see {@link System#arraycopy(Object, int, Object, int, int)}
     **************************************************************************/
    public void read( byte[] buf, int off, int len )
        throws IndexOutOfBoundsException
    {
        System.arraycopy( cache, index, buf, off, len );
        index += len;
    }

    /***************************************************************************
     * Writes the provided byte to the buffer at the current read/write index.
     * @param b the byte to be written
     * @throws IndexOutOfBoundsException If the position is past the end of the
     * cache.
     **************************************************************************/
    public void write( byte b ) throws IndexOutOfBoundsException
    {
        if( index >= cache.length )
        {
            throw new IndexOutOfBoundsException( "Index (" + index +
                ") past/equal to cache length (" + cacheLen + ")" );
        }

        cache[index++] = b;

        if( index > cacheLen )
        {
            cacheLen = index;
        }
    }

    /***************************************************************************
     * Returns the position at which this cache was read from a stream or will
     * be written to a stream.
     * @return the position of this cache in a stream.
     **************************************************************************/
    public long getPosition()
    {
        return position;
    }

    /***************************************************************************
     * Writes the provided buffer to the cache starting at the provided offset
     * into the buffer for the provided length.
     * @param buf the buffer of bytes to be written to the cache.
     * @param off the offset at which to start writing from the provided buffer.
     * @param len the number of bytes to write.
     * @see {@link System#arraycopy(Object, int, Object, int, int)}
     **************************************************************************/
    public void write( byte[] buf, int off, int len )
    {
        System.arraycopy( buf, off, cache, index, len );
        index += len;

        if( index >= cacheLen )
        {
            cacheLen = index;
        }
    }

    /***************************************************************************
     * Returns the size of this cache. This is not necessarily the filled
     * length.
     * @return the size of this cache.
     * @see {@link #getReadLength()}
     **************************************************************************/
    public int getSize()
    {
        return cache.length;
    }

    /***************************************************************************
     * Returns the number of bytes in this cache available for reading. This
     * does not take into account the current read/write index.
     **************************************************************************/
    public long getReadLength()
    {
        return cacheLen;
    }

    /***************************************************************************
     * Sets the next read/write index for this cache based on the provided
     * stream position.
     * @param pos the stream position of the next read/write.
     * @throws IndexOutOfBoundsException If the provided position results in an
     * index outside the bounds of this cache.
     **************************************************************************/
    public void setPosition( long pos ) throws IndexOutOfBoundsException
    {
        int i = ( int )( pos - position );

        if( i < 0 || i > cache.length )
        {
            throw new IndexOutOfBoundsException( "Invalid position " + pos +
                " for this cache of " + cache.length + " bytes at position " +
                position + " results in a local invalic cache index of " + i );
        }

        index = i;
    }
}
