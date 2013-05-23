package org.jutils.utils;

import java.util.Iterator;
import java.util.LinkedList;

// TODO Rename to UniqueMaxStack

/*******************************************************************************
 *
 ******************************************************************************/
public class UniqueMaxStack<T> implements Iterable<T>
{
    public static final int DEFAULT_MAX_SIZE = 20;

    private final LinkedList<T> stack;

    private final int maxSize;

    /***************************************************************************
     *
     **************************************************************************/
    public UniqueMaxStack()
    {
        this( DEFAULT_MAX_SIZE );
    }

    /***************************************************************************
     * @param maxSize int
     **************************************************************************/
    public UniqueMaxStack( int maxSize )
    {
        this.stack = new LinkedList<T>();
        this.maxSize = maxSize;
    }

    public void clear()
    {
        stack.clear();
    }

    public T first()
    {
        return stack.getFirst();
    }

    public boolean isEmpty()
    {
        return stack.isEmpty();
    }

    public void push( T element )
    {
        int index = stack.indexOf( element );

        if( index > -1 )
        {
            stack.remove( index );
        }

        while( stack.size() >= maxSize )
        {
            stack.removeLast();
        }

        stack.addFirst( element );
    }

    public boolean remove( Object o )
    {
        return stack.remove( o );
    }

    public int size()
    {
        return stack.size();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Iterator<T> iterator()
    {
        return stack.iterator();
    }
}
