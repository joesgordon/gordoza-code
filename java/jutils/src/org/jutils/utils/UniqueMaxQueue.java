package org.jutils.utils;

import java.util.Collection;
import java.util.LinkedList;

/*******************************************************************************
 *
 ******************************************************************************/
public class UniqueMaxQueue<T> extends LinkedList<T>
{
    private int maxSize = 20;

    /***************************************************************************
     *
     **************************************************************************/
    public UniqueMaxQueue()
    {
        this( 20 );
    }

    /***************************************************************************
     * @param maxSize int
     **************************************************************************/
    public UniqueMaxQueue( int maxSize )
    {
        super();
        this.maxSize = maxSize;
    }

    /***************************************************************************
	 * 
	 **************************************************************************/
    @Override
    public void addFirst( T element )
    {
        int index = this.indexOf( element );
        if( index > -1 )
        {
            this.remove( index );
        }

        while( size() >= maxSize )
        {
            this.removeLast();
        }
        super.addFirst( element );
    }

    /***************************************************************************
	 * 
	 **************************************************************************/
    @Override
    public void addLast( T element )
    {
        addFirst( element );
    }

    /***************************************************************************
     * @param element E
     **************************************************************************/
    @Override
    public boolean add( T element )
    {
        int index = this.indexOf( element );
        if( index > -1 )
        {
            this.remove( index );
        }

        while( size() >= maxSize )
        {
            this.removeLast();
        }
        super.addFirst( element );
        return true;
    }

    /***************************************************************************
	 * 
	 **************************************************************************/
    @Override
    public void add( int index, T element )
    {
        this.add( element );
    }

    /***************************************************************************
	 * 
	 **************************************************************************/
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

    /***************************************************************************
     * TODO This looks wonky. Either fix it or comment it.
     **************************************************************************/
    @Override
    public boolean addAll( int index, Collection<? extends T> c )
    {
        return addAll( c );
    }

    /***************************************************************************
     * TODO This looks wonky. Either fix it or comment it.
     **************************************************************************/
    @Override
    public T set( int index, T element )
    {
        addFirst( element );
        return null;
    }
}
