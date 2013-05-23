package org.jutils.utils;

import java.util.ArrayDeque;
import java.util.Iterator;

public class MaxQueue<T> implements Iterable<T>
{
    /**  */
    private final ArrayDeque<T> elements;
    /**  */
    private final int maxCount;

    /**
     * @param maxElementCount
     */
    public MaxQueue( int maxElementCount )
    {
        this.maxCount = maxElementCount;
        this.elements = new ArrayDeque<T>( maxElementCount );
    }

    @Override
    public Iterator<T> iterator()
    {
        return elements.iterator();
    }

    public int size()
    {
        return elements.size();
    }

    public boolean isEmpty()
    {
        return elements.isEmpty();
    }

    public boolean remove( Object o )
    {
        return elements.remove( o );
    }

    public void clear()
    {
        elements.clear();
    }

    public boolean push( T e )
    {
        boolean changed = elements.add( e );

        if( size() > maxCount )
        {
            elements.remove();
        }

        return changed;
    }

    public T first()
    {
        return elements.getFirst();
    }
}
