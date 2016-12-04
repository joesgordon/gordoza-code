package org.jutils.ui.fields;

import javax.swing.JComponent;

import org.jutils.io.parsers.DoubleParser;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.validation.IValidationField;
import org.jutils.ui.validation.ValidationTextView;
import org.jutils.ui.validators.DataTextValidator;
import org.jutils.ui.validators.ITextValidator;

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
     **************************************************************************/
    public DoubleFormField( String name, String units, Double min, Double max )
    {
        this( name, units, 20, null, min, max );
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

        textValidator = new DataTextValidator<>( new DoubleParser( min, max ),
            new ValueUpdater( this ) );
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
        this.value = value == null ? this.value : value;

        String text = value == null ? "" : "" + value;
        IUpdater<Double> updater = this.updater;

        this.updater = null;
        textField.setText( text );
        this.updater = updater;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    @Override
    public IValidationField getValidationField()
    {
        return textField.getField();
    }

    /***************************************************************************
     * @param editable
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        textField.getField().setEditable( editable );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
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
