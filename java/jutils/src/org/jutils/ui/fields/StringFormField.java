package org.jutils.ui.fields;

import java.awt.Component;

import javax.swing.JTextField;

import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.validation.ValidationTextView;
import org.jutils.ui.validators.*;

/*******************************************************************************
 * Defines an {@link IFormField} that contains a double validater.
 ******************************************************************************/
public class StringFormField implements IDataFormField<String>
{
    /**  */
    private final String name;
    /**  */
    private final ValidationTextView textField;

    /**  */
    private IUpdater<String> updater;
    /**  */
    private String value;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public StringFormField( String name )
    {
        this( name, 20, 1, null );
    }

    /***************************************************************************
     * @param name
     * @param columns
     **************************************************************************/
    public StringFormField( String name, int columns, Integer minLen,
        Integer maxLen )
    {
        this.name = name;
        this.textField = new ValidationTextView( null, columns );

        this.updater = null;

        ITextValidator textValidator;
        IDataValidator<String> dataValidator;
        IUpdater<String> updater = new ValueUpdater( this );

        dataValidator = new StringLengthValidator( minLen, maxLen );
        textValidator = new DataTextValidator<>( dataValidator, updater );
        textField.getField().setValidator( textValidator );
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
    public Component getField()
    {
        return textField.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getValue()
    {
        return value;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setValue( String value )
    {
        this.value = value;
        textField.setText( "" + value );
    }

    /***************************************************************************
     * @param editable
     **************************************************************************/
    public void setEditable( boolean editable )
    {
        textField.getField().setEditable( editable );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public IValidationField getValidationField()
    {
        return textField.getField();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public JTextField getTextField()
    {
        return textField.getField().getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<String> updater )
    {
        this.updater = updater;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ValueUpdater implements IUpdater<String>
    {
        private final StringFormField view;

        public ValueUpdater( StringFormField view )
        {
            this.view = view;
        }

        @Override
        public void update( String data )
        {
            view.value = data;
            if( view.updater != null )
            {
                view.updater.update( data );
            }
        }
    }
}
