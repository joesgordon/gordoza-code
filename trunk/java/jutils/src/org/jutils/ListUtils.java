package org.jutils;

import java.util.ArrayList;
import java.util.List;

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
}
