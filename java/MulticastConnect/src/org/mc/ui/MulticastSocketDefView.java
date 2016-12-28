package org.mc.ui;

import java.awt.Component;

import org.jutils.ui.StandardFormView;
import org.jutils.ui.event.updater.ReflectiveUpdater;
import org.jutils.ui.fields.IDataFormField;
import org.jutils.ui.fields.IntegerFormField;
import org.jutils.ui.model.IDataView;
import org.mc.io.Ip4Address;
import org.mc.io.MulticastSocketDef;
import org.mc.io.parsers.MulticastGroupParser;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MulticastSocketDefView implements IDataView<MulticastSocketDef>
{
    /**  */
    private final StandardFormView form;
    /**  */
    private final IDataFormField<Ip4Address> addressField;
    /**  */
    private final IntegerFormField portField;
    /**  */
    private final IDataFormField<String> nicField;
    /**  */
    private final IntegerFormField ttlField;

    /**  */
    private MulticastSocketDef def;

    /***************************************************************************
     * 
     **************************************************************************/
    public MulticastSocketDefView()
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
        form.addField( ttlField );

        setData( new MulticastSocketDef() );

        addressField.setUpdater(
            new ReflectiveUpdater<>( this, "def.address" ) );
        portField.setUpdater( new ReflectiveUpdater<>( this, "def.port" ) );
        nicField.setUpdater( new ReflectiveUpdater<>( this, "def.nic" ) );
        ttlField.setUpdater( new ReflectiveUpdater<>( this, "def.ttl" ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Component getView()
    {
        return form.getView();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public MulticastSocketDef getData()
    {
        return def;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( MulticastSocketDef data )
    {
        this.def = data;

        addressField.setValue( def.address );
        portField.setValue( def.port );
        nicField.setValue( def.nic );
        ttlField.setValue( def.ttl );
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
