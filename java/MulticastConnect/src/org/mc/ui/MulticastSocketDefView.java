package org.mc.ui;

import java.awt.Component;
import java.net.NetworkInterface;

import org.jutils.ui.StandardFormView;
import org.jutils.ui.fields.IDataFormField;
import org.jutils.ui.model.IDataView;
import org.mc.io.Ip4Address;
import org.mc.io.MulticastSocketDef;

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
    private final IDataFormField<NetworkInterface> nicField;

    /**  */
    private MulticastSocketDef def;

    /***************************************************************************
     * 
     **************************************************************************/
    public MulticastSocketDefView()
    {
        this.form = new StandardFormView();
        this.addressField = new Ip4AddressField();
        this.nicField = new NetworkInterfaceField( "NIC" );

        form.addField( nicField );
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
    }
}
