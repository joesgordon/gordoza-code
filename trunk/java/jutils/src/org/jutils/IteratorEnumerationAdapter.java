package org.jutils;

import java.util.Enumeration;
import java.util.Iterator;

public class IteratorEnumerationAdapter<T> implements Enumeration<T>
{
    private Iterator<T> it;

    public IteratorEnumerationAdapter( Iterator<T> it )
    {
        this.it = it;
    }

    @Override
    public boolean hasMoreElements()
    {
        return it.hasNext();
    }

    @Override
    public T nextElement()
    {
        return it.next();
    }
}
