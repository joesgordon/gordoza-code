package org.jutils.ui.event;

import java.util.LinkedList;


public class EventListenerList
{
    private LinkedList<IEventListener> listeners;

    public EventListenerList()
    {
        listeners = new LinkedList<IEventListener>();
    }

    public void addListener( IEventListener l )
    {
        listeners.addFirst( l );
    }

    public void removeListener( IEventListener l )
    {
        listeners.remove( l );
    }

    public void fireListeners()
    {
        for( IEventListener l : listeners )
        {
            l.eventOccurred();
        }
    }

    public int size()
    {
        return listeners.size();
    }
}
