package org.jutils.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jutils.IconConstants;
import org.jutils.SwingUtils;
import org.jutils.ui.event.*;
import org.jutils.ui.model.CollectionListModel;
import org.jutils.ui.model.IDataView;

/*******************************************************************************
 * Defines a view that displays a list of items to the user. The view allows for
 * additions/deletions to the list.
 ******************************************************************************/
public class ListView<T> implements IDataView<List<T>>
{
    /** The main component. */
    private final JPanel view;
    /** The model of the list. */
    private final CollectionListModel<DisplayItem<T>> itemsListModel;
    /** The component to display the list. */
    private final JList<DisplayItem<T>> itemsList;
    /** The scroll pane containing the items. */
    private final JScrollPane itemsPane;
    /** The model for the items. */
    private final IItemListModel<T> itemsModel;

    /**  */
    private final Action addAction;
    /**  */
    private final Action removeAction;
    /**  */
    private final Action upAction;
    /**  */
    private final Action downAction;
    /**  */
    private final JToolBar toolbar;

    /**  */
    private final ItemActionList<T> selectedListeners;
    /**  */
    private final ItemActionList<ItemChange<T>> changeListeners;

    /** The items to be displayed. */
    private List<T> items;

    /***************************************************************************
     * Creates a new view with the provided data view and model.
     * @param itemsModel the model for this view.
     **************************************************************************/
    public ListView( IItemListModel<T> itemsModel )
    {
        this( itemsModel, true, true );
    }

    /***************************************************************************
     * @param itemsModel
     * @param canAddRemove
     * @param canOrder
     **************************************************************************/
    public ListView( IItemListModel<T> itemsModel, boolean canAddRemove,
        boolean canOrder )
    {
        this.itemsModel = itemsModel;

        this.itemsListModel = new CollectionListModel<>();
        this.itemsList = new JList<>( itemsListModel );
        this.itemsPane = new JScrollPane( itemsList );
        this.items = new ArrayList<>();
        this.addAction = canAddRemove ? createAddAction() : null;
        this.removeAction = canAddRemove ? createRemoveAction() : null;
        this.upAction = canOrder ? createUpAction() : null;
        this.downAction = canOrder ? createDownAction() : null;

        this.toolbar = createButtonsPanel();

        this.selectedListeners = new ItemActionList<>();
        this.changeListeners = new ItemActionList<>();

        this.view = createView();
    }

    /***************************************************************************
     * Creates the main view.
     **************************************************************************/
    private JPanel createView()
    {
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( toolbar, constraints );

        // itemsPane.setBorder( new EmptyBorder( 0, 0, 0, 0 ) );

        itemsList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        itemsList.addListSelectionListener( new ItemSelctedListener<>( this ) );

        setItemsSize( 200, 200 );

        constraints = new GridBagConstraints( 0, 1, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 0, 0, 0, 0 ), 0, 0 );
        panel.add( itemsPane, constraints );

        return panel;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createAddAction()
    {
        Icon icon = IconConstants.getIcon( IconConstants.EDIT_ADD_16 );
        ActionListener listener = new AddItemListener<T>( this );
        return new ActionAdapter( listener, "Add", icon );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createRemoveAction()
    {
        Icon icon = IconConstants.getIcon( IconConstants.EDIT_DELETE_16 );
        ActionListener listener = new DeleteItemListener<>( this );
        return new ActionAdapter( listener, "Delete", icon );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createUpAction()
    {
        Icon icon = IconConstants.getIcon( IconConstants.NAV_UP_16 );
        ActionListener listener = new MoveUpListener<T>( this );
        return new ActionAdapter( listener, "Move Up", icon );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private Action createDownAction()
    {
        Icon icon = IconConstants.getIcon( IconConstants.NAV_DOWN_16 );
        ActionListener listener = new MoveDownListener<T>( this );
        return new ActionAdapter( listener, "Move Down", icon );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addSelectedListener( ItemActionListener<T> l )
    {
        selectedListeners.addListener( l );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addListChangedListener( ItemActionListener<ItemChange<T>> l )
    {
        changeListeners.addListener( l );
    }

    /***************************************************************************
     * @param button
     **************************************************************************/
    public void addToToolbar( JButton button )
    {
        toolbar.add( button );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void addSeparatorToToolbar()
    {
        toolbar.addSeparator();
    }

    /***************************************************************************
     * Sets the width and height of the items scroll pane.
     * @param width the width of the scroll pane.
     * @param height the height of the scroll pane.
     **************************************************************************/
    public void setItemsSize( int width, int height )
    {
        Dimension dim = new Dimension( width, height );

        itemsPane.setMinimumSize( dim );
        itemsPane.setPreferredSize( dim );
        itemsPane.setMaximumSize( dim );
    }

    /***************************************************************************
     * Creates the component that provides add/remove buttons.
     **************************************************************************/
    private JToolBar createButtonsPanel()
    {
        JToolBar toolbar = new JToolBar();

        SwingUtils.setToolbarDefaults( toolbar );

        SwingUtils.addActionToToolbar( toolbar, addAction );
        SwingUtils.addActionToToolbar( toolbar, removeAction );

        if( toolbar.getComponentCount() > 0 )
        {
            toolbar.addSeparator();
        }

        SwingUtils.addActionToToolbar( toolbar, upAction );
        SwingUtils.addActionToToolbar( toolbar, downAction );

        return toolbar;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JPanel getView()
    {
        return view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public List<T> getData()
    {
        ArrayList<T> data = new ArrayList<>( items );
        return data;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( List<T> data )
    {
        this.items = data;

        List<DisplayItem<T>> ditems = DisplayItem.createList( data,
            itemsModel );

        itemsListModel.setData( ditems );
        itemsList.clearSelection();
    }

    /***************************************************************************
     * Sets the renderer for the list.
     * @param renderer the list cell renderer.
     **************************************************************************/
    public void setItemRenderer( ItemListCellRenderer<T> renderer )
    {
        itemsList.setCellRenderer( new DisplayItemRenderer<T>( renderer ) );
    }

    /***************************************************************************
     * @param type
     * @return
     **************************************************************************/
    public String promptForName( String type )
    {
        String message = "Enter " + type + " name";

        return promptForString( message );
    }

    /***************************************************************************
     * Prompts for a name of an item using a {@link JOptionPane}.
     * @param type the type of item that needs a name (cat, dog, bird, etc.).
     * @return the name entered by the user or {@code null} if cancelled.
     **************************************************************************/
    public String promptForString( String message )
    {
        String name = "";
        boolean prompt = true;

        while( prompt )
        {
            name = JOptionPane.showInputDialog( view, message, name );

            if( name != null )
            {
                if( !name.isEmpty() )
                {
                    if( !contains( name ) )
                    {
                        prompt = false;
                    }
                    else
                    {
                        JOptionPane.showMessageDialog( view,
                            "The name " + name +
                                " already exists. Please Choose a different one.",
                            "Name Exists", JOptionPane.ERROR_MESSAGE );
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog( view,
                        "The name cannot be empty.", "Name Empty",
                        JOptionPane.ERROR_MESSAGE );
                }
            }
            else
            {
                prompt = false;
            }
        }

        return name;
    }

    /***************************************************************************
     * @param name
     * @return
     **************************************************************************/
    private boolean contains( String name )
    {
        for( DisplayItem<T> t : itemsListModel )
        {
            if( name.equals( t.toString() ) )
            {
                return true;
            }
        }
        return false;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public T getSelected()
    {
        DisplayItem<T> item = itemsList.getSelectedValue();

        if( item != null )
        {
            return item.item;
        }

        return null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void clear()
    {
        itemsListModel.clear();
    }

    /***************************************************************************
     * @param enabled
     **************************************************************************/
    public void setEnabled( boolean enabled )
    {
        itemsList.setEnabled( enabled );
        addAction.setEnabled( enabled );
        removeAction.setEnabled( enabled );
        upAction.setEnabled( enabled );
        downAction.setEnabled( enabled );
    }

    /***************************************************************************
     * @param item
     **************************************************************************/
    public void setSelected( T item )
    {
        if( item != null )
        {
            itemsList.setSelectedValue( item, false );
        }
        else
        {
            itemsList.clearSelection();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void refreshSelected()
    {
        int index = itemsList.getSelectedIndex();

        if( index > -1 )
        {
            itemsListModel.refreshIndex( index );
        }
    }

    /***************************************************************************
     * The model used for this view. This model does not provide the data, but
     * provides methods of accessing said data.
     **************************************************************************/
    public static interface IItemListModel<T>
    {
        /** Returns the string representation of the provided item. */
        public String getTitle( T item );

        /** Prompts the user for a new item. */
        public T promptForNew( ListView<T> view );
    }

    /***************************************************************************
     * Defines a class that contains the item along with a method for obtaining
     * a string representation of that item that is not necessarily
     * {@link Object#toString()}.
     **************************************************************************/
    private static class DisplayItem<T>
    {
        public final T item;
        private final IItemListModel<T> model;

        public DisplayItem( T item, IItemListModel<T> model )
        {
            this.item = item;
            this.model = model;
        }

        @Override
        public String toString()
        {
            return model.getTitle( item );
        }

        public static <P> List<DisplayItem<P>> createList( List<P> items,
            IItemListModel<P> model )
        {
            List<DisplayItem<P>> ditems = new ArrayList<>();

            for( P p : items )
            {
                ditems.add( new DisplayItem<P>( p, model ) );
            }

            return ditems;
        }
    }

    /***************************************************************************
     * Defines the listener to be called when an item is selected.
     **************************************************************************/
    private static class ItemSelctedListener<T> implements ListSelectionListener
    {
        private final ListView<T> view;

        public ItemSelctedListener( ListView<T> view )
        {
            this.view = view;
        }

        @Override
        public void valueChanged( ListSelectionEvent e )
        {
            if( !e.getValueIsAdjusting() )
            {
                DisplayItem<T> di = view.itemsList.getSelectedValue();
                T item = di == null ? null : di.item;

                view.selectedListeners.fireListeners( view, item );
            }
        }
    }

    /***************************************************************************
     * Defines the listener to be called when the add button is pressed.
     **************************************************************************/
    private static class AddItemListener<T> implements ActionListener
    {
        private final ListView<T> itemListView;

        public AddItemListener( ListView<T> itemListView )
        {
            this.itemListView = itemListView;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            T item = itemListView.itemsModel.promptForNew( itemListView );

            if( item != null )
            {
                itemListView.itemsListModel.add(
                    new DisplayItem<T>( item, itemListView.itemsModel ) );
                itemListView.items.add( item );
                itemListView.changeListeners.fireListeners( itemListView,
                    new ItemChange<>( ChangeType.ADDED, item ) );
            }
        }
    }

    /***************************************************************************
     * Defines the listener to be called when the delete button is pressed.
     **************************************************************************/
    private static class DeleteItemListener<T> implements ActionListener
    {
        private final ListView<T> itemListView;

        public DeleteItemListener( ListView<T> itemListView )
        {
            this.itemListView = itemListView;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            int index = itemListView.itemsList.getSelectedIndex();

            if( index > -1 )
            {
                itemListView.itemsListModel.remove( index );
                T item = itemListView.items.remove( index );
                itemListView.changeListeners.fireListeners( itemListView,
                    new ItemChange<>( ChangeType.REMOVED, item ) );
            }
        }
    }

    /***************************************************************************
     * @param <T>
     **************************************************************************/
    private static class MoveUpListener<T> implements ActionListener
    {
        private final ListView<T> view;

        public MoveUpListener( ListView<T> view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            int idx = view.itemsList.getSelectedIndex();

            if( idx > 0 )
            {
                T item = view.items.remove( idx );
                view.items.add( idx - 1, item );

                DisplayItem<T> di = view.itemsListModel.remove( idx );
                view.itemsListModel.add( di, idx - 1 );

                view.itemsList.setSelectedIndex( idx - 1 );
            }
        }
    }

    /***************************************************************************
     * @param <T>
     **************************************************************************/
    private static class MoveDownListener<T> implements ActionListener
    {
        private final ListView<T> view;

        public MoveDownListener( ListView<T> view )
        {
            this.view = view;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            int idx = view.itemsList.getSelectedIndex();

            if( idx > -1 && idx < ( view.items.size() - 1 ) )
            {
                T item = view.items.remove( idx );
                view.items.add( idx + 1, item );

                DisplayItem<T> di = view.itemsListModel.remove( idx );
                view.itemsListModel.add( di, idx + 1 );

                view.itemsList.setSelectedIndex( idx + 1 );
            }
        }
    }

    /***************************************************************************
     * Defines a cell renderer.
     * @param <T> The type of item to be added to the list.
     **************************************************************************/
    public static interface ItemListCellRenderer<T>
    {
        public Component getListCellRendererComponent( JList<?> list, T value,
            int index, boolean isSelected, boolean cellHasFocus, String text );
    }

    /***************************************************************************
     * Defines an Adapter to be a renderer for the DisplayItem<T> list that uses
     * a {@link ItemListCellRenderer} to render the cell.
     * @param <T> The type of item to be added to the list.
     **************************************************************************/
    private static class DisplayItemRenderer<T>
        implements ListCellRenderer<DisplayItem<T>>
    {
        private final ItemListCellRenderer<T> renderer;

        public DisplayItemRenderer( ItemListCellRenderer<T> renderer )
        {
            this.renderer = renderer;
        }

        @Override
        public Component getListCellRendererComponent(
            JList<? extends DisplayItem<T>> list, DisplayItem<T> value,
            int index, boolean isSelected, boolean cellHasFocus )
        {
            String text = null;
            T t = null;

            if( value != null )
            {
                text = value.toString();
                t = value.item;
            }

            return renderer.getListCellRendererComponent( list, t, index,
                isSelected, cellHasFocus, text );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static enum ChangeType
    {
        ADDED,
        REMOVED;
    }

    /***************************************************************************
     * @param <T>
     **************************************************************************/
    public static final class ItemChange<T>
    {
        public final ChangeType type;
        public final T item;

        public ItemChange( ChangeType type, T item )
        {
            this.type = type;
            this.item = item;
        }
    }
}
