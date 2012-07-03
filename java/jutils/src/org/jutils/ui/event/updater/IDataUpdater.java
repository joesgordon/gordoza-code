package org.jutils.ui.event.updater;

/*******************************************************************************
 * This interface is a generic way to sync any data with a particular event.
 ******************************************************************************/
public interface IDataUpdater<T>
{
    /***************************************************************************
     * Updates data.
     **************************************************************************/
    public void updateData( T data );
}
