package org.jutils.ui.event.updater;

import java.util.LinkedList;

public class DataUpdaterList<T>
{
    private LinkedList<IDataUpdater<T>> listeners;

    public DataUpdaterList()
    {
        listeners = new LinkedList<IDataUpdater<T>>();
    }

    public void addListener( IDataUpdater<T> l )
    {
        listeners.addFirst( l );
    }

    public void removeListener( IDataUpdater<T> l )
    {
        listeners.remove( l );
    }

    public void fireListeners( T data )
    {
        for( IDataUpdater<T> l : listeners )
        {
            l.updateData( data );
        }
    }

    public int size()
    {
        return listeners.size();
    }
}
