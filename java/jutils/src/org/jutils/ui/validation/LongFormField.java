package org.jutils.ui.validation;

import java.awt.Component;

import javax.swing.JTextField;

import org.jutils.ui.StandardFormView.IFormField;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.validators.*;

/*******************************************************************************
 * Defines an {@link IFormField} that contains a double validater.
 ******************************************************************************/
public class LongFormField implements IFormField
{
    /**  */
    private final String name;
    /**  */
    private final ValidationTextView textField;
    /**  */
    private final IUpdater<Long> updater;

    /**  */
    private long value;

    /***************************************************************************
     * @param name
     * @param units
     * @param columns
     **************************************************************************/
    public LongFormField( String name )
    {
        this( name, ( String )null );
    }

    /***************************************************************************
     * @param name
     * @param updater
     **************************************************************************/
    public LongFormField( String name, IUpdater<Long> updater )
    {
        this( name, null, 20, updater );
    }

    /***************************************************************************
     * @param name
     * @param units
     **************************************************************************/
    public LongFormField( String name, String units )
    {
        this( name, units, 20, null );
    }

    /***************************************************************************
     * @param name
     * @param units
     * @param columns
     **************************************************************************/
    public LongFormField( String name, String units, int columns )
    {
        this( name, units, columns, null );
    }

    /***************************************************************************
     * @param name
     * @param units
     * @param columns
     * @param updater
     **************************************************************************/
    public LongFormField( String name, String units, int columns,
        IUpdater<Long> updater )
    {
        this.name = name;
        this.textField = new ValidationTextView( units, columns );
        this.updater = updater;

        ITextValidator textValidator;

        textValidator = new DataTextValidator<>( new LongValidator(),
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
    public long getValue()
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
        textField.getField().getView().setEditable( false );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ValueUpdater implements IUpdater<Long>
    {
        private final LongFormField view;

        public ValueUpdater( LongFormField view )
        {
            this.view = view;
        }

        @Override
        public void update( Long data )
        {
            view.value = data;
            if( view.updater != null )
            {
                view.updater.update( data );
            }
        }
    }
}
