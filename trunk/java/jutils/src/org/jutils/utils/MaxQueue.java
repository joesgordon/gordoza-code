package org.jutils.utils;

import java.util.*;

/*******************************************************************************
 * Defines a queue that contain zero to the provided maximum number of unique
 * elements.
 * @param <T> The type of element in this queue.
 ******************************************************************************/
public class MaxQueue<T> implements Iterable<T>
{
    /** The collection elements in this queue. */
    private final ArrayDeque<T> elements;
    /** The maximum number of elements in this queue. */
    private final int maxCount;

    /***************************************************************************
     * Creates a new queue to contain up to the provided number of unique
     * elements.
     * @param maxElementCount the maximum number of unique elements in this
     * queue.
     **************************************************************************/
    public MaxQueue( int maxElementCount )
    {
        this.maxCount = maxElementCount;
        this.elements = new ArrayDeque<T>( maxElementCount );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Iterator<T> iterator()
    {
        return elements.iterator();
    }

    /***************************************************************************
     * Returns the number of elements in this queue.
     * @return the number of elements in this queue.
     **************************************************************************/
    public int size()
    {
        return elements.size();
    }

    /***************************************************************************
     * Returns {@code true} if this queue contains no elements.
     * @return {@code true} if this queue contains no elements.
     **************************************************************************/
    public boolean isEmpty()
    {
        return elements.isEmpty();
    }

    /***************************************************************************
     * Removes a single instance of the specified element from this queue. If
     * the queue does not contain the element, it is unchanged. More formally,
     * removes the first element {@code e} such that {@code o.equals(e)} (if
     * such an element exists). Returns {@code true} if this queue contained the
     * specified element (or equivalently, if this queue changed as a result of
     * the call).
     * @param o the element to be removed from this queue, if present.
     * @return {@code true} if this queue contained the specified element
     **************************************************************************/
    public boolean remove( Object o )
    {
        return elements.remove( o );
    }

    /***************************************************************************
     * Removes all of the elements from this queue. The queue will be empty
     * after this call returns.
     **************************************************************************/
    public void clear()
    {
        elements.clear();
    }

    /***************************************************************************
     * Adds the provided element to the beginning of the queue only if it is not
     * already added.
     * @param e the element to be added.
     * @return {@code true}, if the queue was modified, {@code false} otherwise.
     **************************************************************************/
    public boolean push( T e )
    {
        boolean changed = false;

        if( !elements.contains( e ) )
        {
            changed = elements.add( e );

            if( size() > maxCount )
            {
                elements.remove();
            }
        }

        return changed;
    }

    /***************************************************************************
     * Adds the provided items to this queue. If the size of the provided list
     * is larger than the maximum size of this queue, only the last items up to
     * max size will be added.
     * @param items the items to be added to this queue.
     **************************************************************************/
    public void addAll( List<T> items )
    {
        for( T t : items )
        {
            push( t );
        }
    }

    /***************************************************************************
     * Returns the last item in the queue, or {@code null} if empty.
     * @return the last item in the queue, or {@code null} if empty.
     **************************************************************************/
    public T last()
    {
        return elements.isEmpty() ? null : elements.getLast();
    }
}
