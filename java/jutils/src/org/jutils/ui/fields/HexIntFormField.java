package org.jutils.ui.fields;

import java.awt.Component;

import javax.swing.JTextField;

import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.validation.ValidationTextView;
import org.jutils.ui.validators.*;

/*******************************************************************************
 * Defines an {@link IFormField} that contains a double validater.
 ******************************************************************************/
public class HexIntFormField implements IDataFormField<Integer>
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
    public HexIntFormField( String name )
    {
        this( name, ( String )null );
    }

    /***************************************************************************
     * @param name
     * @param updater
     **************************************************************************/
    public HexIntFormField( String name, IUpdater<Integer> updater )
    {
        this( name, null, 20, updater );
    }

    /***************************************************************************
     * @param name
     * @param units
     **************************************************************************/
    public HexIntFormField( String name, String units )
    {
        this( name, units, 20, null );
    }

    /***************************************************************************
     * @param name
     * @param units
     * @param columns
     **************************************************************************/
    public HexIntFormField( String name, String units, int columns )
    {
        this( name, units, columns, null );
    }

    /***************************************************************************
     * @param name
     * @param units
     * @param min
     * @param max
     **************************************************************************/
    public HexIntFormField( String name, String units, Integer min,
        Integer max )
    {
        this( name, units, 20, null, min, max );
    }

    /***************************************************************************
     * @param name
     * @param units
     * @param columns
     * @param updater
     **************************************************************************/
    public HexIntFormField( String name, String units, int columns,
        IUpdater<Integer> updater )
    {
        this( name, units, columns, updater, null, null );
    }

    /***************************************************************************
     * @param name
     * @param units
     * @param columns
     * @param updater
     **************************************************************************/
    public HexIntFormField( String name, String units, int columns,
        IUpdater<Integer> updater, Integer min, Integer max )
    {
        this.name = name;
        this.textField = new ValidationTextView( units, columns );
        this.updater = updater;

        ITextValidator textValidator;

        textValidator = new DataTextValidator<>(
            new HexIntegerValidator( min, max ), new ValueUpdater( this ) );
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
    public Integer getValue()
    {
        return value;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setValue( Integer value )
    {
        this.value = value;
        textField.setText( Integer.toHexString( value ).toUpperCase() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<Integer> updater )
    {
        this.updater = updater;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IUpdater<Integer> getUpdater()
    {
        return updater;
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
        private final HexIntFormField view;

        public ValueUpdater( HexIntFormField view )
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
}
