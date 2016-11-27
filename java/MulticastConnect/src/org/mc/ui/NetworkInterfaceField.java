package org.mc.ui;

import java.net.*;
import java.util.*;

import javax.swing.JComponent;

import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.fields.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class NetworkInterfaceField implements IDataFormField<NetworkInterface>
{
    /**  */
    private final ComboFormField<NetworkInterface> nicField;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public NetworkInterfaceField( String name )
    {
        this.nicField = new ComboFormField<>( name, buildNicList(),
            new NicDescriptor() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private List<NetworkInterface> buildNicList()
    {
        List<NetworkInterface> nics = new ArrayList<>();
        Enumeration<NetworkInterface> nets;

        nics.add( null );

        try
        {
            nets = NetworkInterface.getNetworkInterfaces();

            for( NetworkInterface nic : Collections.list( nets ) )
            {
                Enumeration<InetAddress> inetAddresses = nic.getInetAddresses();
                if( inetAddresses.hasMoreElements() )
                {
                    nics.add( nic );
                }
            }
        }
        catch( SocketException ex )
        {
            ;
        }

        return nics;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getFieldName()
    {
        return nicField.getFieldName();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getField()
    {
        return nicField.getField();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public NetworkInterface getValue()
    {
        return nicField.getValue();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setValue( NetworkInterface value )
    {
        nicField.setValue( value );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<NetworkInterface> updater )
    {
        nicField.setUpdater( updater );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IUpdater<NetworkInterface> getUpdater()
    {
        return nicField.getUpdater();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IValidationField getValidationField()
    {
        return nicField.getValidationField();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setEditable( boolean editable )
    {
        nicField.setEditable( editable );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class NicDescriptor implements IDescriptor<NetworkInterface>
    {
        @Override
        public String getDescription( NetworkInterface nic )
        {
            if( nic == null )
            {
                return "<< Use Routing Table >>";
            }

            return nic.getDisplayName();
        }
    }
}
