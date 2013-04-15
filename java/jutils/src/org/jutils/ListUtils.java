package org.jutils;

import java.util.*;

public final class ListUtils
{
    private ListUtils()
    {
    }

    public static <T> List<T> asList( Iterable<T> items )
    {
        ArrayList<T> list = new ArrayList<T>();

        for( T item : items )
        {
            list.add( item );
        }

        return list;
    }

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

    public static interface IKeyValue<K, V, T>
    {
        public K getKey( T item );

        public V getValue( T item, V oldValue );
    }
}
