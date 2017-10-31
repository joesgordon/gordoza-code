package org.jutils.ui.net;

import javax.swing.JComponent;

import org.jutils.net.TcpInputs;
import org.jutils.ui.StandardFormView;
import org.jutils.ui.fields.*;
import org.jutils.ui.model.IDataView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TcpInputsView implements IDataView<TcpInputs>
{
    /**  */
    private final StandardFormView form;
    /**  */
    private final IDataFormField<String> nicField;
    /**  */
    private final IntegerFormField localPortField;
    /**  */
    private final Ip4AddressField remoteAddressField;
    /**  */
    private final IntegerFormField remotePortField;
    /**  */
    private final IntegerFormField timeoutField;

    /**  */
    private TcpInputs inputs;

    /***************************************************************************
     * 
     **************************************************************************/
    public TcpInputsView( boolean isServer )
    {
        this( isServer, true );
    }

    /***************************************************************************
     * @param advanced shows time-to-live and timeout fields.
     **************************************************************************/
    public TcpInputsView( boolean isServer, boolean advanced )
    {
        this.form = new StandardFormView();

        this.localPortField = new IntegerFormField( "Local Port", 0, 65535 );
        this.nicField = new NetworkInterfaceField( "NIC" );

        this.remoteAddressField = new Ip4AddressField( "Remote Address" );
        this.remotePortField = new IntegerFormField( "Remote Port", 0, 65535 );

        this.timeoutField = new IntegerFormField( "Timeout", "ms", 0, null );

        form.addField( localPortField );
        form.addField( nicField );

        if( !isServer )
        {
            form.addField( remoteAddressField );
            form.addField( remotePortField );
        }

        if( advanced )
        {
            form.addField( timeoutField );
        }

        setData( new TcpInputs() );

        localPortField.setUpdater( ( d ) -> inputs.localPort = d );
        nicField.setUpdater( ( d ) -> inputs.nic = d );
        timeoutField.setUpdater( ( d ) -> inputs.timeout = d );
        remoteAddressField.setUpdater( ( d ) -> inputs.remoteAddress.set( d ) );
        remotePortField.setUpdater( ( d ) -> inputs.remotePort = d );
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
    public TcpInputs getData()
    {
        return inputs;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setData( TcpInputs data )
    {
        this.inputs = data;

        localPortField.setValue( inputs.localPort );
        nicField.setValue( inputs.nic );

        timeoutField.setValue( inputs.timeout );

        remoteAddressField.setValue( inputs.remoteAddress );
        remotePortField.setValue( inputs.remotePort );
    }

    /***************************************************************************
     * @param enabled
     **************************************************************************/
    public void setEnabled( boolean enabled )
    {
        localPortField.setEditable( enabled );
        nicField.setEditable( enabled );

        timeoutField.setEditable( enabled );

        remoteAddressField.setEditable( enabled );
        remotePortField.setEditable( enabled );
    }
}