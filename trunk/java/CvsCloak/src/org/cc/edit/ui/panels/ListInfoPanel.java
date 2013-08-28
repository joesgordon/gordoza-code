package org.cc.edit.ui.panels;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.cc.edit.ui.InfoPanel;
import org.jutils.IconConstants;
import org.jutils.ui.event.ItemActionList;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.model.CollectionListModel;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ListInfoPanel<T> extends InfoPanel<List<T>>
{
    /**  */
    private CollectionListModel<T> dataModel;
    /**  */
    private JList<T> dataList;
    /**  */
    private JButton addButton;
    /**  */
    private JButton removeButton;
    /**  */
    private JButton upButton;
    /**  */
    private JButton downButton;
    /**  */
    private ItemActionList<T> removeListeners;

    /***************************************************************************
     * 
     **************************************************************************/
    public ListInfoPanel()
    {
        super( new BorderLayout() );

        removeListeners = new ItemActionList<T>();
        dataModel = new CollectionListModel<T>();

        dataList = new JList<T>( dataModel );
        JScrollPane listScrollPane = new JScrollPane( dataList );
        JToolBar toolbar = new JToolBar();

        dataList.addListSelectionListener( new ItemSelectionListener() );

        addButton = new JButton(
            IconConstants.loader.getIcon( IconConstants.EDIT_ADD_16 ) );
        removeButton = new JButton(
            IconConstants.loader.getIcon( IconConstants.EDIT_DELETE_16 ) );
        upButton = new JButton(
            IconConstants.loader.getIcon( IconConstants.UP_16 ) );
        downButton = new JButton(
            IconConstants.loader.getIcon( IconConstants.DOWN_16 ) );

        removeButton.setEnabled( false );
        removeButton.addActionListener( new RemoveListener() );
        upButton.setEnabled( false );
        upButton.addActionListener( new UpDownListener( true ) );
        downButton.setEnabled( false );
        downButton.addActionListener( new UpDownListener( false ) );

        toolbar.add( addButton );
        toolbar.add( removeButton );
        toolbar.addSeparator();
        toolbar.add( upButton );
        toolbar.add( downButton );

        toolbar.setFloatable( false );
        toolbar.setRollover( true );
        toolbar.setBorderPainted( false );

        add( toolbar, BorderLayout.NORTH );
        add( listScrollPane, BorderLayout.CENTER );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public final void addAddItemListener( ActionListener l )
    {
        addButton.addActionListener( l );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public final void removeAddItemListener( ActionListener l )
    {
        addButton.removeActionListener( l );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public final void addItemRemovedListener( ItemActionListener<T> l )
    {
        removeListeners.addListener( l );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public final void removeItemRemovedListener( ItemActionListener<T> l )
    {
        removeListeners.removeListener( l );
    }

    /***************************************************************************
     * @param t
     **************************************************************************/
    public void addItem( T t )
    {
        getData().add( t );
        dataModel.add( t );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    protected final void displayData( List<T> data )
    {
        dataModel.clear();

        if( data != null )
        {
            for( T t : data )
            {
                dataModel.add( t );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class ItemSelectionListener implements ListSelectionListener
    {
        @Override
        public void valueChanged( ListSelectionEvent e )
        {
            if( !e.getValueIsAdjusting() )
            {
                int index = dataList.getSelectedIndex();

                removeButton.setEnabled( index > -1 );
                upButton.setEnabled( index > 0 );
                downButton.setEnabled( index < dataModel.getSize() - 1 &&
                    index > -1 );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class RemoveListener implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            int index = dataList.getSelectedIndex();

            if( index > -1 )
            {
                T t = ( T )dataModel.remove( index );

                removeListeners.fireListeners( ListInfoPanel.this, t );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class UpDownListener implements ActionListener
    {
        private int direction;

        public UpDownListener( boolean isUp )
        {
            direction = isUp ? -1 : 1;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            int index = dataList.getSelectedIndex();
            int newIndex = index + direction;

            if( index > -1 && newIndex > -1 && newIndex < dataModel.getSize() )
            {
                T obj = dataModel.remove( index );
                dataModel.add( obj, newIndex );
                dataList.setSelectedIndex( newIndex );
            }
        }
    }
}
