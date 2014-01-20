package org.jutils.utils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.CRC32;

import org.jutils.io.*;
import org.jutils.io.UserOptionsSerializer.IUserOptionsCreator;

//TODO comments

public class FileDataCache<T>
{
    private final IUserOptionsCreator<T> dataCreator;
    private final Map<Long, CacheItem<T>> cache;

    public FileDataCache( IUserOptionsCreator<T> dataCreator )
    {
        this.dataCreator = dataCreator;
        this.cache = new HashMap<>();
    }

    public T getData( File f ) throws FileNotFoundException, IOException
    {
        CacheItem<T> ci = null;
        long cksum;

        ci = cache.get( f.length() );

        cksum = determineFileHash( f );

        if( ci != null )
        {
            if( ci.fileHash != cksum )
            {
                ci = null;
            }
        }

        if( ci == null )
        {
            ci = new CacheItem<>();
            ci.item = dataCreator.createDefaultOptions();
            ci.fileHash = cksum;
            ci.fileLength = f.length();
            cache.put( ci.fileHash, ci );
        }

        return ci.item;
    }

    private long determineFileHash( File file ) throws FileNotFoundException,
        IOException
    {
        byte[] data = new byte[1024];
        CRC32 crc32 = new CRC32();
        IStream stream = new FileStream( file, true );

        int read = stream.read( data );

        crc32.update( data, 0, read );

        return crc32.getValue();
    }

    private static class CacheItem<T>
    {
        public long fileLength;
        public long fileHash;
        public T item;

        @Override
        public int hashCode()
        {
            return ( int )( fileHash + fileLength );
        }

        @Override
        public boolean equals( Object obj )
        {
            if( obj == null )
            {
                return false;
            }
            else if( obj == this )
            {
                return true;
            }
            else if( !( obj instanceof CacheItem ) )
            {
                return false;
            }

            CacheItem<?> c = ( CacheItem<?> )obj;

            return fileLength == c.fileLength && fileHash == c.fileHash;
        }
    }
}
