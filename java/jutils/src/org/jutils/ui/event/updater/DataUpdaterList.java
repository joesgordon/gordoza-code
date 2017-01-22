package org.jutils.ui.event.updater;

import java.util.LinkedList;

/*******************************************************************************
 *
 ******************************************************************************/
public class DataUpdaterList<T>
{
    /**  */
    private LinkedList<IUpdater<T>> listeners;

    /***************************************************************************
     *
     **************************************************************************/
    public DataUpdaterList()
    {
        listeners = new LinkedList<IUpdater<T>>();
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addListener( IUpdater<T> l )
    {
        listeners.addFirst( l );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void removeListener( IUpdater<T> l )
    {
        listeners.remove( l );
    }

    /***************************************************************************
     * @param data
     **************************************************************************/
    public void fireListeners( T data )
    {
        for( IUpdater<T> l : listeners )
        {
            l.update( data );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int size()
    {
        return listeners.size();
    }
}
