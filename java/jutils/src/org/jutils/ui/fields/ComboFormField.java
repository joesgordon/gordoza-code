package org.jutils.ui.fields;

import java.util.Arrays;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.ListCellRenderer;

import org.jutils.ValidationException;
import org.jutils.io.IParser;
import org.jutils.ui.event.updater.ComboBoxUpdater;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.validation.*;

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
    public String getName()
    {
        return name;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getView()
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
    public void setEditable( boolean editable )
    {
        field.getView().setEnabled( editable );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        field.addValidityChanged( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        field.removeValidityChanged( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return field.getValidity();
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
    public void setUserEditable( IParser<T> parser )
    {
        if( parser != null )
        {
            parser = new ParserListener<T>( parser, this );
        }

        field.setEditable( parser );
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

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ParserListener<T> implements IParser<T>
    {
        private final IParser<T> parser;
        private final ComboFormField<T> field;

        public ParserListener( IParser<T> parser, ComboFormField<T> field )
        {
            this.parser = parser;
            this.field = field;
        }

        @Override
        public T parse( String str ) throws ValidationException
        {
            T item = parser.parse( str );

            if( field.updater != null )
            {
                field.updater.update( item );
            }

            return item;
        }
    }
}
