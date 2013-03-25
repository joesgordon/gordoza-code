package org.jutils.io;

import java.io.IOException;
import java.util.Arrays;

/**
 * 
 */
public class ByteCache
{
    /** Default size of 8 MB. */
    public static final int DEFAULT_SIZE = 0x800000;

    /**  */
    private final byte[] cache;

    /**  */
    private long position;
    /**  */
    private int cacheLen;
    /**  */
    private int index;

    /**
     * 
     */
    public ByteCache()
    {
        this( DEFAULT_SIZE );
    }

    /**
     * @param defaultSize
     */
    public ByteCache( int defaultSize )
    {
        this( new byte[defaultSize] );
    }

    /**
     * @param bytes
     */
    public ByteCache( byte[] bytes )
    {
        this.cache = bytes;
        this.position = -1;
    }

    /**
     * 
     */
    protected void printDebug()
    {
        System.out.println( "  bufPos: " + position );
        System.out.println( "bufIndex: " + index );
        System.out.println( "  bufLen: " + cacheLen );
        System.out.println( " bufSize: " + cache.length );
        System.out.println( "   empty: " + ( position < 0 ) );
    }

    /**
     * @return
     */
    public int remainingRead()
    {
        return cacheLen - index;
    }

    /**
     * @return
     */
    public int remainingWrite()
    {
        return cache.length - index;
    }

    /**
     * @param pos
     * @return
     */
    public boolean isReadCached( long pos )
    {
        return position > -1 && pos >= position && pos < position + cacheLen;
    }

    /**
     * @param pos
     * @return
     */
    public boolean isWriteCached( long pos )
    {
        return position > -1 && pos >= position &&
            pos < position + cache.length;
    }

    /**
     * @param stream
     * @throws IOException
     */
    public void readFromStream( IStream stream ) throws IOException
    {
        long pos = stream.getPosition();

        readFromStream( stream, pos );
    }

    /**
     * @param stream
     * @param pos
     * @throws IOException
     */
    public void readFromStream( IStream stream, long pos ) throws IOException
    {
        position = pos;

        System.out.println( "Reading from stream" );
        printDebug();

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

    /**
     * @param stream
     * @throws IOException
     */
    public void writeToStream( IStream stream ) throws IOException
    {
        stream.seek( position );
        stream.write( cache, 0, cacheLen );

        System.out.println( "Writing to Stream" );
        printDebug();
    }

    /**
     * @return
     * @throws IndexOutOfBoundsException
     */
    public byte read() throws IndexOutOfBoundsException
    {
        if( index >= cacheLen )
        {
            throw new IndexOutOfBoundsException( "Index (" + index +
                ") past/equal to cache length (" + cacheLen + ")" );
        }

        return cache[index++];
    }

    /**
     * @param buf
     * @param off
     * @param len
     */
    public void read( byte[] buf, int off, int len )
    {
        System.arraycopy( cache, index, buf, off, len );
        index += len;
    }

    /**
     * @param b
     */
    public void write( byte b )
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

    /**
     * @return
     */
    public long getPosition()
    {
        return position;
    }

    /**
     * @param buf
     * @param off
     * @param len
     */
    public void write( byte[] buf, int off, int len )
    {
        System.arraycopy( buf, off, cache, index, len );
        index += len;

        if( index >= cacheLen )
        {
            cacheLen = index;
        }
    }

    /**
     * @return
     */
    public int getSize()
    {
        return cache.length;
    }

    /**
     * @param pos
     */
    public void setPosition( long pos )
    {
        index = ( int )( pos - position );
    }
}
