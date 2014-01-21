package org.jutils.utils;

import java.io.File;

// TODO comments

// TODO fix me

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public class FileDataCache<T>
{
    /**  */
    private final IDataUtils<T> dataUtils;
    /**  */
    private final MaxQueue<CacheItem<T>> cache;

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
        }

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

            ci = new CacheItem<>();
            ci.item = t;
            ci.path = f.getAbsoluteFile();
            ci.fileLength = f.length();
            cache.push( ci );
        }

        return ci.item;
    }

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

    private static class CacheItem<T>
    {
        public long fileLength;
        public File path;
        public T item;
    }

    public static interface IDataUtils<T>
    {
        T copy( T item );

        T createDefault();
    }
}
