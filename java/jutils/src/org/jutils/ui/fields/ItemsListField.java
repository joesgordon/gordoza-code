package org.jutils.ui.fields;

import java.awt.*;
import java.util.List;

import javax.swing.*;

import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.event.updater.ListUpdater;
import org.jutils.ui.model.CollectionListModel;
import org.jutils.ui.model.LabelListCellRenderer;
import org.jutils.ui.model.LabelListCellRenderer.IListCellLabelDecorator;
import org.jutils.ui.validation.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ItemsListField<T> implements IDataFormField<T>
{
    /** The main component. */
    private final JPanel view;
    /** The model of the list. */
    private final CollectionListModel<T> itemsListModel;
    /** The component to display the list. */
    private final JList<T> itemsList;
    /**  */
    private final ItemCellDecorator<T> decorator;
    /**  */
    private final ValidityListenerList listenerList;

    /**  */
    private IUpdater<T> updater;

    /***************************************************************************
     * @param config
     **************************************************************************/
    public ItemsListField( String name, List<T> choices )
    {
        this( name, choices, null );
    }

    public ItemsListField( String name, List<T> choices,
        IDescriptor<T> descriptor )
    {
        descriptor = descriptor == null ? new DefaultItemDescriptor<>()
            : descriptor;

        this.itemsListModel = new CollectionListModel<>();
        this.itemsList = new JList<>( itemsListModel );
        this.decorator = new ItemCellDecorator<>( descriptor );
        this.listenerList = new ValidityListenerList();

        this.view = createView();

        this.updater = null;

        itemsListModel.addAll( choices );

        itemsList.setName( name );
        itemsList.addListSelectionListener(
            new ListUpdater<T>( ( d ) -> fireUpdaters( d ) ) );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private JPanel createView()
    {
        JScrollPane pane = new JScrollPane( itemsList );
        JPanel panel = new JPanel( new GridBagLayout() );
        GridBagConstraints constraints;

        itemsList.setCellRenderer( new LabelListCellRenderer<>( decorator ) );

        pane.getVerticalScrollBar().setUnitIncrement( 12 );
        pane.setVerticalScrollBarPolicy(
            ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );

        constraints = new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets( 8, 8, 8, 8 ), 0, 0 );
        panel.add( pane, constraints );

        return panel;
    }

    private void fireUpdaters( List<T> selected )
    {
        if( updater != null )
        {
            updater.update( selected.get( 0 ) );
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return view;
    }

    public void setDecorator( IListCellLabelDecorator<T> decorator )
    {
        this.decorator.setDecorator( decorator );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class ItemCellDecorator<T>
        implements IListCellLabelDecorator<T>
    {
        /**  */
        private final IDescriptor<T> descriptor;

        /**  */
        private IListCellLabelDecorator<T> additionalDecorator;

        /**
         * 
         */
        public ItemCellDecorator( IDescriptor<T> descriptor )
        {
            this.descriptor = descriptor;
            this.additionalDecorator = null;
        }

        public void setDecorator( IListCellLabelDecorator<T> decorator )
        {
            this.additionalDecorator = decorator;
        }

        /**
         * {@inheritDoc}
         */
        public void decorate( JLabel label, JList<? extends T> list, T value,
            int index, boolean isSelected, boolean cellHasFocus )
        {
            label.setText( descriptor.getDescription( value ) );

            if( additionalDecorator != null )
            {
                additionalDecorator.decorate( label, list, value, index,
                    isSelected, cellHasFocus );
            }
        }
    }

    @Override
    public T getValue()
    {
        return itemsList.getSelectedValue();
    }

    @Override
    public void setValue( T value )
    {
        itemsList.setSelectedValue( value, true );
    }

    @Override
    public void setUpdater( IUpdater<T> updater )
    {
        this.updater = updater;
    }

    @Override
    public IUpdater<T> getUpdater()
    {
        return updater;
    }

    @Override
    public void setEditable( boolean editable )
    {
        itemsList.setEnabled( editable );
    }

    @Override
    public String getName()
    {
        return itemsList.getName();
    }

    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        listenerList.addListener( l );
    }

    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        listenerList.removeListener( l );
    }

    @Override
    public Validity getValidity()
    {
        return listenerList.getValidity();
    }
}
