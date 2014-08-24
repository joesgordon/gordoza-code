package org.jutils.ui.event.updater;

import org.jutils.ui.event.ItemActionEvent;
import org.jutils.ui.event.ItemActionListener;

public class ItemActionUpdater<T> implements ItemActionListener<T>
{
    private final IUpdater<T> updater;

    public ItemActionUpdater( IUpdater<T> updater )
    {
        this.updater = updater;
    }

    @Override
    public void actionPerformed( ItemActionEvent<T> event )
    {
        updater.update( event.getItem() );
    }
}
