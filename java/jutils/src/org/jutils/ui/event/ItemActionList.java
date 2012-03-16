package org.jutils.ui.event;

import java.util.LinkedList;

public class ItemActionList<T>
{
    private LinkedList<ItemActionListener<T>> listeners;

    public ItemActionList()
    {
        listeners = new LinkedList<ItemActionListener<T>>();
    }

    public void addListener( ItemActionListener<T> l )
    {
        listeners.addFirst( l );
    }

    public void removeListener( ItemActionListener<T> l )
    {
        listeners.remove( l );
    }

    public void fireListeners( Object source, T item )
    {
        ItemActionEvent<T> evt = new ItemActionEvent<T>( source, item );

        for( ItemActionListener<T> l : listeners )
        {
            l.actionPerformed( evt );
        }
    }

    public int size()
    {
        return listeners.size();
    }
}
