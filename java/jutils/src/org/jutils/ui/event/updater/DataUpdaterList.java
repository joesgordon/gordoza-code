package org.jutils.ui.event.updater;

import java.util.LinkedList;

public class DataUpdaterList
{
    private LinkedList<IDataUpdater> listeners;

    public DataUpdaterList()
    {
        listeners = new LinkedList<IDataUpdater>();
    }

    public void addListener( IDataUpdater l )
    {
        listeners.addFirst( l );
    }

    public void removeListener( IDataUpdater l )
    {
        listeners.remove( l );
    }

    public void fireListeners()
    {
        for( IDataUpdater l : listeners )
        {
            l.updateData();
        }
    }

    public int size()
    {
        return listeners.size();
    }
}
