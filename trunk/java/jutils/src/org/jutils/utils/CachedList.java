package org.jutils.utils;

import java.io.IOException;
import java.util.*;

import org.jutils.io.IDataStream;
import org.jutils.io.IStdSerializer;

/*******************************************************************************
 * Provides random access to a group of constant-size items and serializes all
 * but a certain number of items to the provided stream.
 * @param <T> the type of item in the group.
 ******************************************************************************/
public class CachedList<T> implements Iterable<T>
{
    /** The serializer error reporter for this list. */
    private final ICacher<T> cacher;
    /** The stream to be serialized to/from for this list. */
    private final IDataStream stream;
    /** The number of item to store in each cache. */
    private final int cacheCount;
    /** The current item cache. */
    private final ArrayList<T> cache;

    /** The index of the current cache. */
    private int cachedIndex;
    /** The size of the entire list. */
    private int size;
    /**
     * {@code true} if the loaded cache needs to be written to disk;
     * {@code false} otherwise.
     */
    private boolean unwritten;
    /** {@code true} if the stream is open; {@code false} otherwise. */
    private boolean open;

    /***************************************************************************
     * @param cacher
     * @param stream
     **************************************************************************/
    public CachedList( ICacher<T> cacher, IDataStream stream )
    {
        this( cacher, stream, 8 * 1024 * 1024 / cacher.getItemSize() );
    }

    /***************************************************************************
     * @param cacher
     * @param stream
     * @param cacheCount
     **************************************************************************/
    public CachedList( ICacher<T> cacher, IDataStream stream, int cacheCount )
    {
        this.cacher = cacher;
        this.stream = stream;
        this.cacheCount = cacheCount;
        this.cache = new ArrayList<>( cacheCount );

        this.cachedIndex = 0;
        this.unwritten = true;
        this.open = true;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Iterator<T> iterator()
    {
        return new CacheIterator<>( this );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void close()
    {
        cache.clear();

        try
        {
            stream.close();
        }
        catch( IOException ex )
        {
            throw new RuntimeException( "Error closing cache file", ex );
        }

        open = false;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isEmpty()
    {
        return size == 0;
    }

    /***************************************************************************
     * @param item
     **************************************************************************/
    public void add( T item )
    {
        if( !open )
        {
            throw new IllegalStateException(
                "Cannot add items when stream closed" );
        }

        ensureCached( size++ );

        cache.add( item );

        unwritten = true;
    }

    /***************************************************************************
     * @param index
     **************************************************************************/
    public T get( int index )
    {
        if( !open )
        {
            throw new IllegalStateException(
                "Cannot add items when stream closed" );
        }

        ensureCached( index );

        return cache.get( index - cachedIndex );
    }

    /***************************************************************************
     * Returns a view of the portion of this list between the specified
     * {@code fromIndex}, inclusive, and {@code toIndex}, exclusive.
     * @param fromIndex
     * @param toIndex
     * @return
     * @see List#subList(int, int)
     **************************************************************************/
    public List<T> subList( int fromIndex, int toIndex )
    {
        int len = toIndex - fromIndex;
        ArrayList<T> list = new ArrayList<>( len );

        for( int i = fromIndex; i < toIndex; i++ )
        {
            list.add( get( i ) );
        }

        return list;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int size()
    {
        return size;
    }

    /***************************************************************************
     * @param index
     **************************************************************************/
    private void ensureCached( int index )
    {
        if( index < 0 || index > size )
        {
            throw new ArrayIndexOutOfBoundsException(
                "Invalid index into list of size " + size + ": " + index );
        }

        if( index >= cachedIndex && index < cachedIndex + cacheCount )
        {
            return;
        }

        loadCacheSafe( index );
    }

    /***************************************************************************
     * @param index
     **************************************************************************/
    private void loadCacheSafe( int index )
    {
        try
        {
            // -----------------------------------------------------------------
            // Write cache if necessary.
            // -----------------------------------------------------------------
            if( unwritten )
            {
                // LogUtils.printDebug( "writing @ " + index );
                writeCache();

                unwritten = false;
            }

            // -----------------------------------------------------------------
            // Read cache if necessary
            // -----------------------------------------------------------------
            // LogUtils.printDebug( "reading @ " + index );
            readCache( index );
        }
        catch( IOException ex )
        {
            throw new RuntimeException( "Error loading cache from file", ex );
        }
    }

    /***************************************************************************
     * @param index
     * @throws IOException
     **************************************************************************/
    private void readCache( int index ) throws IOException
    {
        int cacheIndex = index / cacheCount;

        long position = cacheIndex * cacheCount * cacher.getItemSize();

        cache.clear();

        cachedIndex = cacheIndex * cacheCount;

        if( position == stream.getLength() )
        {
            return;
        }

        stream.seek( position );

        for( int i = 0; i < cacheCount && stream.getAvailable() > 0; i++ )
        {
            cache.add( cacher.read( stream ) );
        }

        // LogUtils.printDebug( "   read: " + cache.size() );
    }

    /***************************************************************************
     * @throws IOException
     **************************************************************************/
    private void writeCache() throws IOException
    {
        // LogUtils.printDebug( "    wrote " +
        // ( cache.size() * cacher.getItemSize() ) + " bytes" );

        for( int i = 0; i < cache.size(); i++ )
        {
            cacher.write( cache.get( i ), stream );
        }
    }

    /***************************************************************************
     * Defines the methods of serializing and item to a stream and reporting
     * exceptions.
     * @param <T> the type of item to be serialized.
     **************************************************************************/
    public static interface ICacher<T> extends IStdSerializer<T, IDataStream>
    {
        public int getItemSize();
    }

    /***************************************************************************
     * @param <T>
     **************************************************************************/
    private static class CacheIterator<T> implements Iterator<T>
    {
        private final CachedList<T> list;

        private int index;

        public CacheIterator( CachedList<T> list )
        {
            this.list = list;
        }

        @Override
        public boolean hasNext()
        {
            return index < list.size();
        }

        @Override
        public T next()
        {
            return list.get( index++ );
        }
    }
}
