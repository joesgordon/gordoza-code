package org.jutils.ui.net;

import javax.swing.JComponent;

import org.jutils.io.parsers.MulticastGroupParser;
import org.jutils.net.MulticastInputs;
import org.jutils.ui.StandardFormView;
import org.jutils.ui.event.updater.ReflectiveUpdater;
import org.jutils.ui.fields.*;
import org.jutils.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MulticastInputsView implements IDataView<MulticastInputs>
{
    /**  */
    private final StandardFormView form;
    /**  */
    private final Ip4AddressField addressField;
    /**  */
    private final IntegerFormField portField;
    /**  */
    private final IDataFormField<String> nicField;
    /**  */
    private final IntegerFormField ttlField;

    /**  */
    private MulticastInputs inputs;

    /***************************************************************************
     * 
     **************************************************************************/
    public MulticastInputsView()
    {
        this( true );
    }

    public MulticastInputsView( boolean showTtl )
    {
        this.form = new StandardFormView();
        this.addressField = new Ip4AddressField( "Multicast Group",
            new MulticastGroupParser() );
        this.portField = new IntegerFormField( "Port", 0, 65535 );
        this.nicField = new NetworkInterfaceField( "NIC" );
        this.ttlField = new IntegerFormField( "TTL", 0, 255 );

        form.addField( addressField );
        form.addField( portField );
        form.addField( nicField );
        if( showTtl )
        {
            form.addField( ttlField );
        }

        setData( new MulticastInputs() );

        addressField.setUpdater(
            new ReflectiveUpdater<>( this, "inputs.address" ) );
        portField.setUpdater( new ReflectiveUpdater<>( this, "inputs.port" ) );
        nicField.setUpdater( new ReflectiveUpdater<>( this, "inputs.nic" ) );
        ttlField.setUpdater( new ReflectiveUpdater<>( this, "inputs.ttl" ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return form.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public MulticastInputs getData()
    {
        return inputs;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( MulticastInputs data )
    {
        this.inputs = data;

        addressField.setValue( inputs.address );
        portField.setValue( inputs.port );
        nicField.setValue( inputs.nic );
        ttlField.setValue( inputs.ttl );
    }

    /***************************************************************************
     * @param enabled
     **************************************************************************/
    public void setEnabled( boolean enabled )
    {
        addressField.setEditable( enabled );
        portField.setEditable( enabled );
        nicField.setEditable( enabled );
        ttlField.setEditable( enabled );
    }
}
