package org.mc.ui;

import java.net.*;
import java.util.*;

import javax.swing.JComponent;

import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.fields.*;
import org.jutils.ui.validation.IValidityChangedListener;
import org.jutils.ui.validation.Validity;

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
    public String getName()
    {
        return nicField.getName();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return nicField.getView();
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
    public void setEditable( boolean editable )
    {
        nicField.setEditable( editable );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addValidityChanged( IValidityChangedListener l )
    {
        nicField.addValidityChanged( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void removeValidityChanged( IValidityChangedListener l )
    {
        nicField.removeValidityChanged( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public Validity getValidity()
    {
        return nicField.getValidity();
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
