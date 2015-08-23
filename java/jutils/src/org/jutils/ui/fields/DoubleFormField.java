package org.jutils.ui.fields;

import java.awt.Component;

import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.validation.ValidationTextView;
import org.jutils.ui.validators.*;

/*******************************************************************************
 * Defines an {@link IFormField} that contains a double validater.
 ******************************************************************************/
public class DoubleFormField implements IDataFormField<Double>
{
    /**  */
    private final String name;
    /**  */
    private final ValidationTextView textField;

    /**  */
    private IUpdater<Double> updater;

    /**  */
    private double value;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public DoubleFormField( String name )
    {
        this( name, null );
    }

    /***************************************************************************
     * @param name
     **************************************************************************/
    public DoubleFormField( String name, Double min, Double max )
    {
        this( name, null, 20, null, min, max );
    }

    /***************************************************************************
     * @param name
     * @param units
     **************************************************************************/
    public DoubleFormField( String name, String units )
    {
        this( name, units, 20 );
    }

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
        this( name, units, columns, updater, null, null );
    }

    /***************************************************************************
     * @param name
     * @param units
     * @param columns
     * @param updater
     * @param min
     * @param max
     **************************************************************************/
    public DoubleFormField( String name, String units, int columns,
        IUpdater<Double> updater, Double min, Double max )
    {
        this.name = name;
        this.textField = new ValidationTextView( units, columns );
        this.updater = updater;

        ITextValidator textValidator;

        textValidator = new DataTextValidator<>(
            new DoubleValidator( min, max ), new ValueUpdater( this ) );
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
    public Double getValue()
    {
        return value;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setValue( Double value )
    {
        this.value = value;
        IUpdater<Double> updater = this.updater;
        this.updater = null;
        textField.setText( "" + value );
        this.updater = updater;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public IValidationField getValidationField()
    {
        return textField.getField();
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
    public void setUpdater( IUpdater<Double> updater )
    {
        this.updater = updater;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IUpdater<Double> getUpdater()
    {
        return updater;
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
