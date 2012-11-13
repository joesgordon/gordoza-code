package org.jutils.ui.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;

/*******************************************************************************
 * 
 ******************************************************************************/
public class CollectionListModel<T> extends AbstractListModel
{
    /**  */
    private List<T> items;

    /***************************************************************************
     * 
     **************************************************************************/
    public CollectionListModel()
    {
        items = new ArrayList<T>();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int getSize()
    {
        return items.size();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public T getElementAt( int index )
    {
        return items.get( index );
    }

    /***************************************************************************
     * @param items
     **************************************************************************/
    public void setData( List<T> items )
    {
        int maxIndex = Math.max( this.items.size(), items.size() ) - 1;

        // fireIntervalRemoved( this, 0, getSize() );

        this.items = items;

        // fireIntervalAdded( this, 0, getSize() );

        fireContentsChanged( this, 0, maxIndex );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public List<T> getData()
    {
        return items;
    }

    /***************************************************************************
     * @param item
     **************************************************************************/
    public void add( T item )
    {
        int index = getSize();

        items.add( item );

        fireIntervalAdded( this, index, index );
    }

    /***************************************************************************
     * @param item
     * @param index
     **************************************************************************/
    public void add( T item, int index )
    {
        items.add( index, item );

        fireIntervalAdded( this, index, index );
    }

    /***************************************************************************
     * @param item
     **************************************************************************/
    public void remove( T item )
    {
        int index = items.indexOf( item );

        remove( index );
    }

    /***************************************************************************
     * @param index
     **************************************************************************/
    public T remove( int index )
    {
        T t = items.remove( index );

        fireIntervalRemoved( this, index, index );

        return t;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void clear()
    {
        items.clear();

        fireIntervalRemoved( this, 0, getSize() );
    }
}
