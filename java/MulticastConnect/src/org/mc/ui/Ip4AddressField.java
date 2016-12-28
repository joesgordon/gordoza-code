package org.mc.ui;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;

import org.jutils.io.IParser;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.fields.IDataFormField;
import org.jutils.ui.fields.ParserFormField;
import org.jutils.ui.model.ParserTextFormatter;
import org.jutils.ui.validation.IValidityChangedListener;
import org.jutils.ui.validation.Validity;
import org.mc.io.Ip4Address;
import org.mc.io.parsers.Ip4AddressParser;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Ip4AddressField implements IDataFormField<Ip4Address>
{
    /**  */
    private final IDataFormField<Ip4Address> field;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public Ip4AddressField( String name )
    {
        this( name, new Ip4AddressParser() );
    }

    /***************************************************************************
     * @param name
     * @param parser
     **************************************************************************/
    public Ip4AddressField( String name, IParser<Ip4Address> parser )
    {
        JFormattedTextField textField = new JFormattedTextField(
            new ParserTextFormatter<>( parser ) );

        this.field = new ParserFormField<>( name, parser, textField );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getName()
    {
        return field.getName();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return field.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Ip4Address getValue()
    {
        return field.getValue();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setValue( Ip4Address value )
    {
        field.setValue( value );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<Ip4Address> updater )
    {
        field.setUpdater( updater );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IUpdater<Ip4Address> getUpdater()
    {
        return field.getUpdater();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        field.setEditable( editable );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        field.addValidityChanged( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        field.removeValidityChanged( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return field.getValidity();
    }
}
