package org.budgey.ui;

import javax.swing.JComponent;

import org.budgey.data.Money;
import org.jutils.core.ui.event.updater.IUpdater;
import org.jutils.core.ui.fields.IDataFormField;
import org.jutils.core.ui.fields.ParserFormField;
import org.jutils.core.ui.validation.IValidityChangedListener;
import org.jutils.core.ui.validation.Validity;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MoneyFormField implements IDataFormField<Money>
{
    /**  */
    private final ParserFormField<Money> field;

    /***************************************************************************
     * @param name the name of the form field to be returned by
     * {@link IDataFormField#getName()}.
     **************************************************************************/
    public MoneyFormField( String name )
    {
        this.field = new ParserFormField<>( name, new MoneyParser(),
            ( m ) -> toString( m ) );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String getName()
    {
        return field.getName();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return field.getView();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Money getValue()
    {
        return field.getValue();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setValue( Money value )
    {
        field.setValue( value );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<Money> updater )
    {
        field.setUpdater( updater );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public IUpdater<Money> getUpdater()
    {
        return field.getUpdater();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        field.setEditable( editable );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        field.addValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        field.removeValidityChanged( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return field.getValidity();
    }

    /***************************************************************************
     * @param value
     * @return
     **************************************************************************/
    private String toString( Money value )
    {
        return value == null ? "" : value.toString();
    }
}
