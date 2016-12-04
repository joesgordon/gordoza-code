package org.budgey.ui;

import javax.swing.JComponent;

import org.budgey.data.Money;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.fields.IDataFormField;
import org.jutils.ui.validation.IValidationField;
import org.jutils.ui.validation.ValidationTextView;
import org.jutils.ui.validators.DataTextValidator;
import org.jutils.ui.validators.ITextValidator;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MoneyFormField implements IDataFormField<Money>
{
    /**  */
    private final ValidationTextView field;
    /**  */
    private final String fieldName;

    /**  */
    private Money amount;
    /**  */
    private IUpdater<Money> updater;

    /***************************************************************************
     * @param fieldName
     **************************************************************************/
    public MoneyFormField( String fieldName )
    {
        this.fieldName = fieldName;
        this.field = new ValidationTextView();

        ITextValidator textValidator;

        textValidator = new DataTextValidator<>( new MoneyParser(),
            new ValueUpdater( this ) );
        field.getField().setValidator( textValidator );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getName()
    {
        return fieldName;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getField()
    {
        return field.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Money getValue()
    {
        return amount;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setValue( Money value )
    {
        this.amount = value;

        String text = value == null ? "" : value.toString();

        field.setText( text );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<Money> updater )
    {
        this.updater = updater;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IUpdater<Money> getUpdater()
    {
        return updater;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IValidationField getValidationField()
    {
        return field.getField();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        field.getField().setEditable( editable );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class ValueUpdater implements IUpdater<Money>
    {
        private final MoneyFormField view;

        public ValueUpdater( MoneyFormField view )
        {
            this.view = view;
        }

        @Override
        public void update( Money data )
        {
            view.amount = data;
            if( view.updater != null )
            {
                view.updater.update( data );
            }
        }
    }
}
