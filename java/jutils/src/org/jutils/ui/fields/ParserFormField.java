package org.jutils.ui.fields;

import javax.swing.JComponent;

import org.jutils.io.IParser;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.validation.IValidationField;
import org.jutils.ui.validation.ValidationTextView;
import org.jutils.ui.validators.DataTextValidator;
import org.jutils.ui.validators.ITextValidator;

/*******************************************************************************
 * Defines an {@link IFormField} that allows the user to define an object that
 * has the provided parser.
 ******************************************************************************/
public class ParserFormField<T> implements IDataFormField<T>
{
    /**  */
    private final String name;
    /**  */
    private final ValidationTextView textField;

    /**  */
    private IUpdater<T> updater;
    /**  */
    private T value;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public ParserFormField( String name, IParser<T> parser )
    {
        this.name = name;
        this.textField = new ValidationTextView( null, 20 );

        ITextValidator textValidator;
        IUpdater<T> updater = ( d ) -> update( d );

        textValidator = new DataTextValidator<>( parser, updater );
        textField.getField().setValidator( textValidator );
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
        return textField.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public T getValue()
    {
        return value;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setValue( T value )
    {
        this.value = value;

        String text = value == null ? "" : "" + value;

        textField.setText( text );
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
        return textField.getField();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        textField.getField().setEditable( editable );
    }

    /***************************************************************************
     * @param data
     **************************************************************************/
    private void update( T data )
    {
        value = data;

        if( updater != null )
        {
            updater.update( data );
        }
    }
}
