package org.jutils.ui.fields;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import javax.swing.JComboBox;

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
    private final ItemComboBoxModel<T> model;
    /**  */
    private final JComboBox<T> field;
    /**  */
    private final ValidityListenerList listenerList;

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
        this.model = new ItemComboBoxModel<T>( items );
        this.field = new JComboBox<T>( model );
        this.listenerList = new ValidityListenerList();

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
    public JComboBox<T> getView()
    {
        return field;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public T getSelectedItem()
    {
        return model.getSelectedItem();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public void setSelectedItem( T item )
    {
        model.setSelectedItem( item );
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
}
