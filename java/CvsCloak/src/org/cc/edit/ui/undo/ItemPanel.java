package org.cc.edit.ui.undo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jutils.ui.event.ItemActionList;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.model.CollectionListModel;

/*******************************************************************************
 * @param <T>
 ******************************************************************************/
public class ItemPanel<T> extends JPanel
{
    /**  */
    private ItemActionList<T> itemSelectedListeners;
    /**  */
    private CollectionListModel<T> model;
    /**  */
    private JList<T> itemList;

    /***************************************************************************
     * 
     **************************************************************************/
    public ItemPanel()
    {
        this( null );
    }

    /***************************************************************************
     * @param items
     **************************************************************************/
    public ItemPanel( List<T> items )
    {
        super( new BorderLayout() );

        itemSelectedListeners = new ItemActionList<T>();
        model = new CollectionListModel<T>();
        itemList = new JList<T>( model );
        itemList.setVisibleRowCount( 10 );

        JScrollPane pane = new JScrollPane( itemList );
        Dimension dim = pane.getPreferredSize();

        itemList.addListSelectionListener( new ItemSelectedListener() );
        dim.width = 160;
        pane.setPreferredSize( dim );
        pane.setSize( 100, pane.getHeight() );

        add( pane, BorderLayout.CENTER );

        setItems( items );
    }

    /***************************************************************************
     * @param item
     **************************************************************************/
    public void addItem( T item )
    {
        model.add( item );
    }

    /***************************************************************************
     * TODO create a
     * @param index
     * @return
     **************************************************************************/
    public T removeItem( int index )
    {
        return model.remove( index );
    }

    /***************************************************************************
     * @param item
     **************************************************************************/
    public void removeItem( T item )
    {
        model.remove( item );
    }

    /***************************************************************************
     * @param items
     **************************************************************************/
    public void setItems( List<T> items )
    {
        if( items != null )
        {
            model.addAll( items );
        }
        else
        {
            model.clear();
        }
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addItemSelectedListener( ItemActionListener<T> l )
    {
        itemSelectedListeners.addListener( l );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void removeItemSelectedListener( ItemActionListener<T> l )
    {
        itemSelectedListeners.removeListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class ItemSelectedListener implements ListSelectionListener
    {
        @Override
        public void valueChanged( ListSelectionEvent e )
        {
            if( !e.getValueIsAdjusting() )
            {
                int index = itemList.getSelectedIndex();
                if( index > -1 )
                {
                    itemSelectedListeners.fireListeners( ItemPanel.this,
                        model.get( index ) );
                    itemList.clearSelection();
                }
            }
        }
    }
}
