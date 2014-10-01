package org.jutils.ui.fields;

import java.awt.Component;

import javax.swing.JTextField;

import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.hex.HexUtils;
import org.jutils.ui.validation.ValidationTextView;
import org.jutils.ui.validators.*;

/*******************************************************************************
 * Defines an {@link IFormField} that contains a double validater.
 ******************************************************************************/
public class HexByteFormField implements IDataFormField<Byte>
{
    /**  */
    private final String name;
    /**  */
    private final ValidationTextView textField;

    /**  */
    private IUpdater<Byte> updater;
    /**  */
    private byte value;

    /***************************************************************************
     * @param name
     * @param units
     * @param columns
     **************************************************************************/
    public HexByteFormField( String name )
    {
        this( name, ( String )null );
    }

    /***************************************************************************
     * @param name
     * @param updater
     **************************************************************************/
    public HexByteFormField( String name, IUpdater<Byte> updater )
    {
        this( name, null, 20, updater );
    }

    /***************************************************************************
     * @param name
     * @param units
     **************************************************************************/
    public HexByteFormField( String name, String units )
    {
        this( name, units, 20, null );
    }

    /***************************************************************************
     * @param name
     * @param units
     * @param columns
     **************************************************************************/
    public HexByteFormField( String name, String units, int columns )
    {
        this( name, units, columns, null );
    }

    /***************************************************************************
     * @param name
     * @param units
     * @param min
     * @param max
     **************************************************************************/
    public HexByteFormField( String name, String units, Byte min, Byte max )
    {
        this( name, units, 20, null, min, max );
    }

    /***************************************************************************
     * @param name
     * @param units
     * @param columns
     * @param updater
     **************************************************************************/
    public HexByteFormField( String name, String units, int columns,
        IUpdater<Byte> updater )
    {
        this( name, units, columns, updater, null, null );
    }

    /***************************************************************************
     * @param name
     * @param units
     * @param columns
     * @param updater
     **************************************************************************/
    public HexByteFormField( String name, String units, int columns,
        IUpdater<Byte> updater, Byte min, Byte max )
    {
        this.name = name;
        this.textField = new ValidationTextView( units, columns );
        this.updater = updater;

        ITextValidator textValidator;

        textValidator = new DataTextValidator<>(
            new HexByteValidator( min, max ), new ValueUpdater( this ) );
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
    public Byte getValue()
    {
        return value;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setValue( Byte value )
    {
        this.value = value;
        textField.setText( HexUtils.toHexString( value ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<Byte> updater )
    {
        this.updater = updater;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IUpdater<Byte> getUpdater()
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
    private static class ValueUpdater implements IUpdater<Byte>
    {
        private final HexByteFormField view;

        public ValueUpdater( HexByteFormField view )
        {
            this.view = view;
        }

        @Override
        public void update( Byte data )
        {
            view.value = data;
            if( view.updater != null )
            {
                view.updater.update( data );
            }
        }
    }
}
