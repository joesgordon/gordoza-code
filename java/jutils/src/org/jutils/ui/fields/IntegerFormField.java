package org.jutils.ui.fields;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.jutils.io.parsers.IntegerParser;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.validation.ValidationTextView;
import org.jutils.ui.validators.*;

/*******************************************************************************
 * Defines an {@link IFormField} that contains a integer validator.
 ******************************************************************************/
public class IntegerFormField implements IDataFormField<Integer>
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
     **************************************************************************/
    public IntegerFormField( String name )
    {
        this( name, null );
    }

    /***************************************************************************
     * @param name
     * @param min
     * @param max
     **************************************************************************/
    public IntegerFormField( String name, Integer min, Integer max )
    {
        this( name, null, 20, min, max );
    }

    /***************************************************************************
     * @param name
     * @param units
     **************************************************************************/
    public IntegerFormField( String name, String units )
    {
        this( name, units, 20, null, null );
    }

    /***************************************************************************
     * @param name
     * @param units
     * @param min
     * @param max
     **************************************************************************/
    public IntegerFormField( String name, String units, Integer min,
        Integer max )
    {
        this( name, units, 20, min, max );
    }

    /***************************************************************************
     * @param name
     * @param units
     * @param columns
     **************************************************************************/
    public IntegerFormField( String name, String units, int columns )
    {
        this( name, units, columns, null, null );
    }

    /***************************************************************************
     * @param name
     * @param units
     * @param columns
     * @param min
     * @param max
     **************************************************************************/
    public IntegerFormField( String name, String units, int columns,
        Integer min, Integer max )
    {
        this.name = name;
        this.textField = new ValidationTextView( units, columns );
        this.updater = null;

        ITextValidator textValidator;

        textValidator = new DataTextValidator<>(
            new IntegerParser( min, max ), new ValueUpdater( this ) );
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
        this.value = value == null ? this.value : value;

        String text = value == null ? "" : "" + value;
        IUpdater<Integer> updater = this.updater;

        this.updater = null;
        textField.setText( text );
        this.updater = updater;
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
    @Override
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
    @Override
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
}
