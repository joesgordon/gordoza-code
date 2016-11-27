package org.jutils.ui.fields;

import javax.swing.JComponent;

import org.jutils.io.parsers.BinaryParser;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.validation.ValidationTextView;
import org.jutils.ui.validators.*;
import org.jutils.utils.BitArray;

/*******************************************************************************
 * 
 ******************************************************************************/
public class BitsField implements IDataFormField<BitArray>
{
    /**  */
    private final String name;
    /**  */
    private final ValidationTextView textField;

    /**  */
    public BitArray value;
    /**  */
    private IUpdater<BitArray> updater;

    public BitsField( String name )
    {
        this.name = name;
        this.textField = new ValidationTextView( null, 20 );
        this.value = null;
        this.updater = null;

        ITextValidator textValidator;

        textValidator = new DataTextValidator<>( new BinaryParser(),
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
    public JComponent getField()
    {
        return textField.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public BitArray getValue()
    {
        return value;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setValue( BitArray value )
    {
        this.value = value;

        String text = value == null ? "" : value.toString();

        textField.setText( text );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<BitArray> updater )
    {
        this.updater = updater;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IUpdater<BitArray> getUpdater()
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
     * 
     **************************************************************************/
    private static class ValueUpdater implements IUpdater<BitArray>
    {
        private final BitsField view;

        public ValueUpdater( BitsField view )
        {
            this.view = view;
        }

        @Override
        public void update( BitArray data )
        {
            view.value = data;

            if( view.updater != null )
            {
                view.updater.update( data );
            }
        }
    }
}
