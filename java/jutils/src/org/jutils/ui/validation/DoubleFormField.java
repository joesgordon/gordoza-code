package org.jutils.ui.validation;

import java.awt.Component;

import org.jutils.ui.StandardFormView.IFormField;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.validators.*;

/*******************************************************************************
 * Defines an {@link IFormField} that contains a double validater.
 ******************************************************************************/
public class DoubleFormField implements IFormField
{
    /**  */
    private final String name;
    /**  */
    private final ValidationTextView textField;
    /**  */
    private final IUpdater<Double> updater;

    /**  */
    private double value;

    /***************************************************************************
     * @param name
     * @param units
     * @param columns
     **************************************************************************/
    public DoubleFormField( String name, String units, int columns )
    {
        this( name, units, columns, null );
    }

    /***************************************************************************
     * @param name
     * @param units
     * @param columns
     * @param updater
     **************************************************************************/
    public DoubleFormField( String name, String units, int columns,
        IUpdater<Double> updater )
    {
        this.name = name;
        this.textField = new ValidationTextView( units, columns );
        this.updater = updater;

        ITextValidator textValidator;

        textValidator = new DataTextValidator<>( new DoubleValidator(),
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
    public double getValue()
    {
        return value;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public IValidationField getValidationField()
    {
        return textField.getField();
    }

    /***************************************************************************
     * @param value
     **************************************************************************/
    public void setValue( double value )
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
    private static class ValueUpdater implements IUpdater<Double>
    {
        private final DoubleFormField view;

        public ValueUpdater( DoubleFormField view )
        {
            this.view = view;
        }

        @Override
        public void update( Double data )
        {
            view.value = data;
            if( view.updater != null )
            {
                view.updater.update( data );
            }
        }
    }
}
