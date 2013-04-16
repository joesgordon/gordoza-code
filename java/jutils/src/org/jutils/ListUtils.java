package org.jutils;

import java.util.*;
import java.util.Map.Entry;

import org.jutils.utils.Tuple2;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class ListUtils
{
    /***************************************************************************
     * 
     **************************************************************************/
    private ListUtils()
    {
    }

    /***************************************************************************
     * @param items
     * @return
     **************************************************************************/
    public static <T> List<T> asList( Iterable<T> items )
    {
        ArrayList<T> list = new ArrayList<T>();

        for( T item : items )
        {
            list.add( item );
        }

        return list;
    }

    /***************************************************************************
     * @param items
     * @param kv
     * @return
     **************************************************************************/
    public static <K, V, T> Map<K, V> createMap( List<T> items,
        IKeyValue<K, V, T> kv )
    {
        Map<K, V> map = new HashMap<K, V>();

        for( T item : items )
        {
            K key = kv.getKey( item );

            V value = map.get( key );

            value = kv.getValue( item, value );

            map.put( key, value );
        }

        return map;
    }

    /***************************************************************************
     * @param <K>
     * @param <V>
     * @param <T>
     **************************************************************************/
    public static interface IKeyValue<K, V, T>
    {
        public K getKey( T item );

        public V getValue( T item, V oldValue );
    }

    /***************************************************************************
     * @param map
     * @return
     **************************************************************************/
    public static <K> Tuple2<K, Integer> findMaxEntry( Map<K, Integer> map )
    {
        Entry<K, Integer> maxEntry = null;
        int max = -1;

        for( Entry<K, Integer> entry : map.entrySet() )
        {
            if( entry.getValue() > max )
            {
                max = entry.getValue();
                maxEntry = entry;
            }
        }

        return maxEntry == null ? null : new Tuple2<K, Integer>(
            maxEntry.getKey(), maxEntry.getValue() );
    }
}
