package org.jutils.ui.fields;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ItemListener;
import java.util.*;

import javax.swing.*;

import org.jutils.ui.event.updater.ComboBoxUpdater;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.model.ItemComboBoxModel;
import org.jutils.ui.validation.IValidityChangedListener;
import org.jutils.ui.validation.ValidityListenerList;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class ValidationComboField<T> implements IValidationField
{
    /**  */
    private final ItemComboBoxModel<DescriptorObject> model;
    /**  */
    private final JComboBox<DescriptorObject> field;
    /**  */
    private final ValidityListenerList listenerList;
    /**  */
    private final IDescriptor<T> descriptor;

    /**  */
    private Color validBackground;
    /**  */
    private Color invalidBackground;

    /***************************************************************************
     * @param items
     **************************************************************************/
    public ValidationComboField( T [] items )
    {
        this( Arrays.asList( items ) );
    }

    /***************************************************************************
     * @param items
     **************************************************************************/
    public ValidationComboField( List<T> items )
    {
        this( items, new ObjectDescriptor<T>() );
    }

    /***************************************************************************
     * @param items
     * @param descriptor
     **************************************************************************/
    public ValidationComboField( List<T> items, IDescriptor<T> descriptor )
    {
        this.model = new ItemComboBoxModel<>( buildList( items ) );
        this.field = new JComboBox<>( model );
        this.listenerList = new ValidityListenerList();
        this.descriptor = descriptor != null ? descriptor
            : new ObjectDescriptor<T>();

        this.validBackground = field.getBackground();
        this.invalidBackground = Color.red;

        field.setBackground( validBackground );
        field.addItemListener(
            new ComboBoxUpdater<T>( new ValidationActionListener<T>( this ) ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return field;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public T getSelectedItem()
    {
        DescriptorObject obj = model.getSelectedItem();
        return obj == null ? null : obj.item;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public void setSelectedItem( T item )
    {
        model.setSelectedItem( new DescriptorObject( item ) );
    }

    /***************************************************************************
     * @param items
     **************************************************************************/
    public void setItems( List<T> items )
    {
        model.setItems( buildList( items ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean isValid()
    {
        return listenerList.isValid();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getInvalidationReason()
    {
        return listenerList.getInvalidationReason();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void setComponentValid( boolean valid )
    {
        if( valid )
        {
            field.setBackground( validBackground );
        }
        else
        {
            field.setBackground( invalidBackground );
        }
    }

    /***************************************************************************
     * @param vcl
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener vcl )
    {
        listenerList.addListener( vcl );
    }

    /***************************************************************************
     * @param vcl
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener vcl )
    {
        listenerList.removeListener( vcl );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setValidBackground( Color bg )
    {
        validBackground = bg;
        setComponentValid( listenerList.isValid() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setInvalidBackground( Color bg )
    {
        invalidBackground = bg;
        setComponentValid( listenerList.isValid() );
    }

    /***************************************************************************
     * @param editable
     **************************************************************************/
    public void setEditable( boolean editable )
    {
        field.setEditable( editable );
    }

    /***************************************************************************
     * @param listener
     **************************************************************************/
    public void addItemListener( ItemListener listener )
    {
        field.addItemListener( listener );
    }

    /***************************************************************************
     * @param items
     * @return
     **************************************************************************/
    private List<DescriptorObject> buildList( List<T> items )
    {
        List<DescriptorObject> listObjects = new ArrayList<>();

        for( T item : items )
        {
            listObjects.add( new DescriptorObject( item ) );
        }

        return listObjects;
    }

    /***************************************************************************
     * @param renderer
     **************************************************************************/
    public void setRenderer( ListCellRenderer<Object> renderer )
    {
        field.setRenderer( new DescriptorRenderer( renderer ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ValidationActionListener<T> implements IUpdater<T>
    {
        private ValidationComboField<T> field;

        public ValidationActionListener( ValidationComboField<T> field )
        {
            this.field = field;
        }

        @Override
        public void update( T data )
        {
            Object item = field.field.getSelectedItem();

            if( item == null )
            {
                field.field.setBackground( field.invalidBackground );
                field.listenerList.signalInvalid( "No item chosen" );
            }
            else
            {
                field.field.setBackground( field.validBackground );
                field.listenerList.signalValid();
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class DescriptorObject
    {
        public final T item;

        public DescriptorObject( T item )
        {
            this.item = item;
        }

        public String toString()
        {
            return descriptor.getDescription( item );
        }

        public boolean equals( Object obj )
        {
            if( obj != null )
            {
                if( obj instanceof ValidationComboField.DescriptorObject )
                {
                    @SuppressWarnings( "unchecked")
                    DescriptorObject dobj = ( DescriptorObject )obj;

                    return dobj.item.equals( item );
                }
            }

            return false;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ObjectDescriptor<T> implements IDescriptor<T>
    {
        @Override
        public String getDescription( T item )
        {
            if( item == null )
            {
                return "";
            }

            return item.toString();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private class DescriptorRenderer implements ListCellRenderer<Object>
    {
        private final ListCellRenderer<Object> renderer;

        public DescriptorRenderer( ListCellRenderer<Object> renderer )
        {
            this.renderer = renderer;
        }

        @Override
        public Component getListCellRendererComponent(
            JList<? extends Object> list, Object value, int index,
            boolean isSelected, boolean cellHasFocus )
        {
            return renderer.getListCellRendererComponent( list, value, index,
                isSelected, cellHasFocus );
        }
    }
}
