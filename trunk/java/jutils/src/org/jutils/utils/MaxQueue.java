package org.jutils.utils;

import java.util.*;

public class MaxQueue<T> implements Queue<T>
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
    public int size()
    {
        return elements.size();
    }

    @Override
    public boolean isEmpty()
    {
        return elements.isEmpty();
    }

    @Override
    public boolean contains( Object o )
    {
        return elements.contains( o );
    }

    @Override
    public Iterator<T> iterator()
    {
        return elements.iterator();
    }

    @Override
    public Object[] toArray()
    {
        return elements.toArray();
    }

    @Override
    public <G> G[] toArray( G[] a )
    {
        return elements.toArray( a );
    }

    @Override
    public boolean remove( Object o )
    {
        return elements.remove( o );
    }

    @Override
    public boolean containsAll( Collection<?> c )
    {
        return elements.containsAll( c );
    }

    @Override
    public boolean addAll( Collection<? extends T> c )
    {
        boolean changed = false;

        for( T t : c )
        {
            changed = changed || add( t );
        }

        return changed;
    }

    @Override
    public boolean removeAll( Collection<?> c )
    {
        return elements.removeAll( c );
    }

    @Override
    public boolean retainAll( Collection<?> c )
    {
        return elements.retainAll( c );
    }

    @Override
    public void clear()
    {
        elements.clear();
    }

    @Override
    public boolean add( T e )
    {
        boolean changed = elements.add( e );

        if( size() > maxCount )
        {
            remove();
        }

        return changed;
    }

    @Override
    public boolean offer( T e )
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public T remove()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T poll()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T element()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T peek()
    {
        // TODO Auto-generated method stub
        return null;
    }

}
