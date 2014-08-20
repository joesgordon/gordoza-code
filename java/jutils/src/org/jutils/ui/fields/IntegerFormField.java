package org.jutils.ui.fields;

import java.awt.Component;

import javax.swing.JTextField;

import org.jutils.ui.StandardFormView.IFormField;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.validation.ValidationTextView;
import org.jutils.ui.validators.*;

/*******************************************************************************
 * Defines an {@link IFormField} that contains a double validater.
 ******************************************************************************/
public class IntegerFormField implements IFormField
{
    /**  */
    private final String name;
    /**  */
    private final ValidationTextView textField;

    /**  */
    private IUpdater<Integer> updater;
    /**  */
    private int value;

    /***************************************************************************
     * @param name
     * @param units
     * @param columns
     **************************************************************************/
    public IntegerFormField( String name )
    {
        this( name, ( String )null );
    }

    /***************************************************************************
     * @param name
     * @param updater
     **************************************************************************/
    public IntegerFormField( String name, IUpdater<Integer> updater )
    {
        this( name, null, 20, updater );
    }

    /***************************************************************************
     * @param name
     * @param units
     **************************************************************************/
    public IntegerFormField( String name, String units )
    {
        this( name, units, 20, null );
    }

    /***************************************************************************
     * @param name
     * @param units
     * @param columns
     **************************************************************************/
    public IntegerFormField( String name, String units, int columns )
    {
        this( name, units, columns, null );
    }

    /***************************************************************************
     * @param name
     * @param units
     * @param columns
     * @param updater
     **************************************************************************/
    public IntegerFormField( String name, String units, int columns,
        IUpdater<Integer> updater )
    {
        this.name = name;
        this.textField = new ValidationTextView( units, columns );
        this.updater = updater;

        ITextValidator textValidator;

        textValidator = new DataTextValidator<>( new IntegerValidator(),
            new ValueUpdater( this ) );
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
     * @return
     **************************************************************************/
    public int getValue()
    {
        return value;
    }

    /***************************************************************************
     * @param value
     **************************************************************************/
    public void setValue( int value )
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
     * 
     **************************************************************************/
    private static class ValueUpdater implements IUpdater<Integer>
    {
        private final IntegerFormField view;

        public ValueUpdater( IntegerFormField view )
        {
            this.view = view;
        }

        @Override
        public void update( Integer data )
        {
            view.value = data;
            if( view.updater != null )
            {
                view.updater.update( data );
            }
        }
    }

    public void setUpdater( IUpdater<Integer> updater )
    {
        this.updater = updater;
    }
}
