package org.jutils;

import java.util.Enumeration;
import java.util.Iterator;

public class EnumerationIteratorAdapter<T> implements Iterator<T>, Iterable<T>
{
    private Enumeration<T> it;

    public EnumerationIteratorAdapter( Enumeration<T> it )
    {
        this.it = it;
    }

    @Override
    public boolean hasNext()
    {
        return it.hasMoreElements();
    }

    @Override
    public T next()
    {
        return it.nextElement();
    }

    @Override
    public void remove()
    {
        throw new UnsupportedOperationException(
            "Cannot remove from an Enumeration." );
    }

    @Override
    public Iterator<T> iterator()
    {
        return this;
    }
}
