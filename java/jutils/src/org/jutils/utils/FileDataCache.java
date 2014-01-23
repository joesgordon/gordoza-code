package org.jutils.utils;

import java.io.File;
import java.util.*;

// TODO comments

// TODO fix me

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public class FileDataCache<T> implements Iterable<T>
{
    /**  */
    private final IDataUtils<T> dataUtils;
    /**  */
    private final MaxQueue<CacheItem<T>> cache;

    /***************************************************************************
     * @param dataUtils
     * @param maxCount
     **************************************************************************/
    public FileDataCache( IDataUtils<T> dataUtils, int maxCount )
    {
        this.dataUtils = dataUtils;
        this.cache = new MaxQueue<>( maxCount );
    }

    /***************************************************************************
     * Finds the user data indexed by the provided file.
     * @param f
     * @return
     **************************************************************************/
    public T getData( File f )
    {
        CacheItem<T> ci = null;

        ci = find( f.length() );

        if( ci == null )
        {
            ci = find( f.getAbsoluteFile() );

            if( ci == null )
            {
                T t = null;

                ci = cache.last();

                if( ci == null )
                {
                    t = dataUtils.createDefault();
                }
                else
                {
                    t = dataUtils.copy( ci.item );
                }

                ci = createData( f, t );

                cache.push( ci );
            }
        }

        return ci.item;
    }

    /***************************************************************************
     * @param f
     * @param item
     **************************************************************************/
    public void putData( File f, T item )
    {
        CacheItem<T> ci = createData( f, item );

        cache.push( ci );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public List<T> getData()
    {
        List<T> items = new ArrayList<>( cache.size() );

        for( T item : this )
        {
            items.add( item );
        }

        return items;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Iterator<T> iterator()
    {
        return new CacheItemIterator<>( cache.iterator() );
    }

    /***************************************************************************
     * @param f
     * @param item
     * @return
     **************************************************************************/
    private CacheItem<T> createData( File f, T item )
    {
        CacheItem<T> ci = new CacheItem<>();

        ci.item = item;
        ci.path = f.getAbsoluteFile();
        ci.fileLength = f.length();

        return ci;
    }

    /***************************************************************************
     * @param f
     * @return
     **************************************************************************/
    private CacheItem<T> find( File f )
    {
        for( CacheItem<T> ci : cache )
        {
            if( f.equals( ci.path ) )
            {
                return ci;
            }
        }

        return null;
    }

    /***************************************************************************
     * @param length
     * @return
     **************************************************************************/
    private CacheItem<T> find( long length )
    {
        for( CacheItem<T> ci : cache )
        {
            if( length == ci.fileLength )
            {
                return ci;
            }
        }

        return null;
    }

    /***************************************************************************
     * @param <T>
     **************************************************************************/
    private static class CacheItem<T>
    {
        public long fileLength;
        public File path;
        public T item;
    }

    /***************************************************************************
     * @param <T>
     **************************************************************************/
    private static class CacheItemIterator<T> implements Iterator<T>
    {
        private final Iterator<CacheItem<T>> cii;

        public CacheItemIterator( Iterator<CacheItem<T>> cii )
        {
            this.cii = cii;
        }

        @Override
        public boolean hasNext()
        {
            return cii.hasNext();
        }

        @Override
        public T next()
        {
            return cii.next().item;
        }

        @Override
        public void remove()
        {
            cii.remove();
        }

    }

    /***************************************************************************
     * @param <T>
     **************************************************************************/
    public static interface IDataUtils<T>
    {
        T copy( T item );

        T createDefault();
    }
}
