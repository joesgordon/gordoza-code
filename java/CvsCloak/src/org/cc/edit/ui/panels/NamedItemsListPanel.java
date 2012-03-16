package org.cc.edit.ui.panels;

import javax.swing.undo.UndoManager;

import org.cc.creators.ItemCreator;
import org.jutils.ui.event.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class NamedItemsListPanel<T> extends StringListInfoPanel<T>
{
    private ItemActionList<T> itemAddedListeners;
    private ItemCreator<T> creator;

    /***************************************************************************
     * @param manager
     **************************************************************************/
    public NamedItemsListPanel( UndoManager manager, ItemCreator<T> creator )
    {
        this( manager, creator, "Name?" );
    }

    /***************************************************************************
     * @param manager
     * @param question
     **************************************************************************/
    public NamedItemsListPanel( UndoManager manager, ItemCreator<T> creator,
        String question )
    {
        super( question );

        this.creator = creator;
        addAddItemListener( new ProductAddedListener() );

        itemAddedListeners = new ItemActionList<T>();
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addItemAddedListener( ItemActionListener<T> l )
    {
        itemAddedListeners.addListener( l );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void removeItemAddedListener( ItemActionListener<T> l )
    {
        itemAddedListeners.removeListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class ProductAddedListener implements ItemActionListener<String>
    {
        @Override
        public void actionPerformed( ItemActionEvent<String> event )
        {
            T item = creator.createItem( event.getItem() );
            addItem( item );
            itemAddedListeners.fireListeners( NamedItemsListPanel.this, item );
        }
    }
}
