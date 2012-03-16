package org.jutils.ui.model;

import java.util.*;

import javax.swing.MutableComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class ComboBoxListModel<T> implements List<T>, MutableComboBoxModel
{
    private final List<ListDataListener> ldListeners;
    private final List<T> items;
    private int selectedIndex;

    /***************************************************************************
     * 
     **************************************************************************/
    public ComboBoxListModel()
    {
        this( new ArrayList<T>() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public ComboBoxListModel( List<T> items )
    {
        this.items = new ArrayList<T>( items );
        this.ldListeners = new ArrayList<ListDataListener>();
        this.selectedIndex = -1;
    }

    /***************************************************************************
     * @param idx0
     * @param idx1
     **************************************************************************/
    protected void fireIntervalChanged( int idx0, int idx1 )
    {
        ListDataEvent event = new ListDataEvent( this,
            ListDataEvent.CONTENTS_CHANGED, idx0, idx1 );

        for( int i = ldListeners.size() - 1; i > -1; i-- )
        {
            ListDataListener l = ldListeners.get( i );
            l.contentsChanged( event );
        }
    }

    /***************************************************************************
     * @param idx0
     * @param idx1
     **************************************************************************/
    protected void fireIntervalAdded( int idx0, int idx1 )
    {
        ListDataEvent event = new ListDataEvent( this,
            ListDataEvent.INTERVAL_ADDED, idx0, idx1 );

        if( event.getIndex0() <= selectedIndex )
        {
            selectedIndex += ( event.getIndex1() - event.getIndex0() + 1 );
        }

        for( int i = ldListeners.size() - 1; i > -1; i-- )
        {
            ListDataListener l = ldListeners.get( i );
            l.intervalAdded( event );
        }
    }

    /***************************************************************************
     * @param idx0
     * @param idx1
     **************************************************************************/
    protected void fireIntervalRemoved( int idx0, int idx1 )
    {
        ListDataEvent event = new ListDataEvent( this,
            ListDataEvent.INTERVAL_REMOVED, idx0, idx1 );

        if( event.getIndex1() < selectedIndex )
        {
            selectedIndex -= ( event.getIndex1() - event.getIndex0() + 1 );
        }
        else if( event.getIndex0() <= selectedIndex &&
            selectedIndex <= event.getIndex1() )
        {
            if( event.getIndex0() < getSize() )
            {
                selectedIndex = event.getIndex0();
            }
            else
            {
                selectedIndex = getSize() - 1;
            }
        }

        for( int i = ldListeners.size() - 1; i > -1; i-- )
        {
            ListDataListener l = ldListeners.get( i );
            l.intervalRemoved( event );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Object getSelectedItem()
    {
        return getSelected();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public T getSelected()
    {
        return selectedIndex > -1 ? items.get( selectedIndex ) : null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setSelectedItem( Object anItem )
    {
        try
        {
            @SuppressWarnings( "unchecked")
            T item = ( T )anItem;

            if( item != null && item.toString().length() > 0 )
            {
                selectedIndex = items.indexOf( item );

                if( selectedIndex < 0 )
                {
                    items.add( item );
                    selectedIndex = items.indexOf( item );
                }
            }
            else
            {
                selectedIndex = -1;
            }
            fireIntervalChanged( -1, -1 );
        }
        catch( ClassCastException ex )
        {
            ex.printStackTrace();
            throw new IllegalArgumentException( "Item is not of type 'T': " +
                anItem.toString(), ex );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addListDataListener( ListDataListener l )
    {
        ldListeners.add( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void removeListDataListener( ListDataListener l )
    {
        if( !ldListeners.remove( l ) )
        {
            throw new IllegalArgumentException( "Listener not found: " +
                l.toString() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Object getElementAt( int index )
    {
        return items.get( index );
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
    public boolean add( T e )
    {
        int index = items.size();
        boolean result = items.add( e );

        if( result )
        {
            fireIntervalAdded( index, index );
        }

        return result;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void add( int index, T element )
    {
        items.add( index, element );

        fireIntervalAdded( index, index );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean addAll( Collection<? extends T> c )
    {
        int begin = items.size();
        boolean result = items.addAll( c );
        int end = begin + c.size();

        if( result )
        {
            fireIntervalAdded( begin, end );
        }

        return result;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean addAll( int index, Collection<? extends T> c )
    {
        int begin = index;
        boolean result = items.addAll( index, c );
        int end = begin + c.size();

        if( result )
        {
            fireIntervalAdded( begin, end );
        }

        return result;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void clear()
    {
        int begin = 0;
        int end = items.size() - 1;
        items.clear();

        if( end > -1 )
        {
            fireIntervalRemoved( begin, end );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean contains( Object o )
    {
        return items.contains( o );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean containsAll( Collection<?> c )
    {
        return items.containsAll( c );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public T get( int index )
    {
        return items.get( index );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int indexOf( Object o )
    {
        return items.indexOf( o );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean isEmpty()
    {
        return items.isEmpty();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Iterator<T> iterator()
    {
        return items.iterator();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int lastIndexOf( Object o )
    {
        return items.lastIndexOf( o );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public ListIterator<T> listIterator()
    {
        return items.listIterator();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public ListIterator<T> listIterator( int index )
    {
        return items.listIterator( index );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean remove( Object o )
    {
        int index = items.indexOf( o );
        boolean result = items.remove( o );

        if( result )
        {
            fireIntervalRemoved( index, index );
        }

        return result;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public T remove( int index )
    {
        T t = items.remove( index );

        if( t != null )
        {
            fireIntervalRemoved( index, index );
        }

        return t;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean removeAll( Collection<?> c )
    {
        return items.removeAll( c );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean retainAll( Collection<?> c )
    {
        return items.retainAll( c );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public T set( int index, T element )
    {
        return items.set( index, element );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int size()
    {
        return items.size();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public List<T> subList( int fromIndex, int toIndex )
    {
        return items.subList( fromIndex, toIndex );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Object[] toArray()
    {
        return items.toArray();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public <I> I[] toArray( I[] a )
    {
        return items.toArray( a );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    @SuppressWarnings( "unchecked")
    public void addElement( Object obj )
    {
        boolean result = add( ( T )obj );

        if( !result )
        {
            throw new IllegalArgumentException( "Unable to add " +
                obj.toString() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    @SuppressWarnings( "unchecked")
    public void insertElementAt( Object obj, int index )
    {
        add( index, ( T )obj );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    @SuppressWarnings( "unchecked")
    public void removeElement( Object obj )
    {
        boolean result = remove( ( T )obj );

        if( !result )
        {
            throw new IllegalArgumentException( "Unable to remove " +
                obj.toString() );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void removeElementAt( int index )
    {
        remove( index );
    }
}
