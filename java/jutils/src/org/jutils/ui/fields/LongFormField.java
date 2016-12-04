package org.jutils.ui.fields;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.jutils.io.parsers.LongParser;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.validation.IValidationField;
import org.jutils.ui.validation.ValidationTextView;
import org.jutils.ui.validators.*;

/*******************************************************************************
 * Defines an {@link IFormField} that contains a double validater.
 ******************************************************************************/
public class LongFormField implements IDataFormField<Long>
{
    /**  */
    private final String name;
    /**  */
    private final ValidationTextView textField;

    /**  */
    private IUpdater<Long> updater;
    /**  */
    private Long value;

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
     * @param min
     * @param max
     **************************************************************************/
    public LongFormField( String name, String units, Long min, Long max )
    {
        this( name, units, 20, null, min, max );
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
        this( name, units, columns, updater, null, null );
    }

    /***************************************************************************
     * @param name
     * @param units
     * @param columns
     * @param updater
     **************************************************************************/
    public LongFormField( String name, String units, int columns,
        IUpdater<Long> updater, Long min, Long max )
    {
        this.name = name;
        this.textField = new ValidationTextView( units, columns );
        this.updater = updater;

        ITextValidator textValidator;

        textValidator = new DataTextValidator<>( new LongParser( min, max ),
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
    public JComponent getField()
    {
        return textField.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Long getValue()
    {
        return value;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setValue( Long value )
    {
        this.value = value;

        String text = value == null ? "" : "" + value;

        textField.setText( text );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<Long> updater )
    {
        this.updater = updater;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IUpdater<Long> getUpdater()
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
