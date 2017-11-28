package org.jutils.ui.event;

public interface IRecentListener<T>
{
    public void selected( T item, boolean ctrlPressed );
}
