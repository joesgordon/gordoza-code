package org.jutils.ui.fields;

import java.util.Arrays;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.ListCellRenderer;

import org.jutils.ui.event.updater.ComboBoxUpdater;
import org.jutils.ui.event.updater.IUpdater;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class ComboFormField<T> implements IDataFormField<T>
{
    /**  */
    private final ValidationComboField<T> field;
    /**  */
    private final String name;

    /**  */
    private IUpdater<T> updater;

    /***************************************************************************
     * @param name
     * @param items
     **************************************************************************/
    public ComboFormField( String name, T [] items )
    {
        this( name, items, null );
    }

    /***************************************************************************
     * @param name
     * @param items
     * @param descriptor
     **************************************************************************/
    public ComboFormField( String name, T [] items, IDescriptor<T> descriptor )
    {
        this( name, Arrays.asList( items ), descriptor );
    }

    /***************************************************************************
     * @param name
     * @param items
     **************************************************************************/
    public ComboFormField( String name, List<T> items )
    {
        this( name, items, null );
    }

    /***************************************************************************
     * @param name
     * @param items
     * @param descriptor
     **************************************************************************/
    public ComboFormField( String name, List<T> items,
        IDescriptor<T> descriptor )
    {
        this.field = new ValidationComboField<>( items, descriptor );
        this.name = name;

        this.updater = null;

        field.addItemListener(
            new ComboBoxUpdater<>( new ComboValidListener<T>( this ) ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getFieldName()
    {
        return name;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getField()
    {
        return field.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public T getValue()
    {
        return field.getSelectedItem();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setValue( T value )
    {
        field.setSelectedItem( value );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<T> updater )
    {
        this.updater = updater;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IUpdater<T> getUpdater()
    {
        return updater;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IValidationField getValidationField()
    {
        return field;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        field.getView().setEnabled( editable );
    }

    /***************************************************************************
     * @param r
     **************************************************************************/
    public void setRenderer( ListCellRenderer<Object> r )
    {
        field.setRenderer( r );
    }

    /***************************************************************************
     * @param items
     **************************************************************************/
    public void setValues( List<T> items )
    {
        field.setItems( items );
    }

    /***************************************************************************
     * @param editable
     **************************************************************************/
    public void setUserEditable( boolean editable )
    {
        field.setEditable( editable );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ComboValidListener<T> implements IUpdater<T>
    {
        private ComboFormField<T> field;

        public ComboValidListener( ComboFormField<T> field )
        {
            this.field = field;
        }

        @Override
        public void update( T data )
        {
            if( field.updater != null )
            {
                field.updater.update( field.getValue() );
            }
        }
    }
}
