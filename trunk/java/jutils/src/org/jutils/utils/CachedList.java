package org.jutils.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.jutils.io.IStdSerializer;
import org.jutils.io.IStream;

/*******************************************************************************
 * 
 ******************************************************************************/
public class CachedList<T> implements Iterable<T>
{
    /**  */
    private final ICacher<T> cacher;
    /**  */
    private final IStream stream;
    /**  */
    private final int cacheCount;
    /**  */
    private final ArrayList<T> cache;

    /**  */
    private int cachedIndex;
    /**  */
    private int size;
    /**  */
    private boolean unwritten;

    /***************************************************************************
     * @param cacher
     * @param stream
     **************************************************************************/
    public CachedList( ICacher<T> cacher, IStream stream )
    {
        this( cacher, stream, 8 * 1024 * 1024 / cacher.getItemSize() );
    }

    /***************************************************************************
     * @param cacher
     * @param stream
     * @param cacheCount
     **************************************************************************/
    public CachedList( ICacher<T> cacher, IStream stream, int cacheCount )
    {
        this.cacher = cacher;
        this.stream = stream;
        this.cacheCount = cacheCount;
        this.cache = new ArrayList<>( cacheCount );

        this.cachedIndex = 0;
        this.unwritten = true;
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
     * @param item
     **************************************************************************/
    public void add( T item )
    {
        ensureCached( size++ );

        cache.add( item );

        unwritten = true;
    }

    /***************************************************************************
     * @param index
     **************************************************************************/
    public T get( int index )
    {
        ensureCached( index );

        return cache.get( index - cachedIndex );
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
            cacher.reportException( ex );
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
     * @param <T>
     **************************************************************************/
    public static interface ICacher<T> extends IStdSerializer<T, IStream>
    {
        public int getItemSize();

        public void reportException( IOException ex );
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
