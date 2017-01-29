package org.jutils.ui.fields;

import java.net.*;
import java.util.*;

import javax.swing.JComponent;

import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.validation.IValidityChangedListener;
import org.jutils.ui.validation.Validity;

/*******************************************************************************
 * 
 ******************************************************************************/
public class NetworkInterfaceField implements IDataFormField<String>
{
    /**  */
    private final ComboFormField<NetworkInterface> nicField;
    /**  */
    private final NicUpdater updater;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public NetworkInterfaceField( String name )
    {
        this.nicField = new ComboFormField<>( name, buildNicList(),
            new NicDescriptor() );

        this.updater = new NicUpdater();

        nicField.setUpdater( updater );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static List<NetworkInterface> buildNicList()
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
    public String getValue()
    {
        NetworkInterface nic = nicField.getValue();
        return nic == null ? null : nic.getName();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setValue( String value )
    {

        NetworkInterface nic = null;

        if( value != null )
        {
            try
            {
                nic = NetworkInterface.getByName( value );
            }
            catch( SocketException ex )
            {
                nic = null;
            }
        }

        nicField.setValue( nic );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<String> updater )
    {
        this.updater.setUpdater( updater );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IUpdater<String> getUpdater()
    {
        return updater.getUpdater();
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

    /***************************************************************************
     * 
     **************************************************************************/
    private final static class NicUpdater implements IUpdater<NetworkInterface>
    {
        private IUpdater<String> updater;

        @Override
        public void update( NetworkInterface data )
        {
            if( updater != null )
            {
                String name = data == null ? null : data.getName();
                updater.update( name );
            }
        }

        public IUpdater<String> getUpdater()
        {
            return updater;
        }

        public void setUpdater( IUpdater<String> updater )
        {
            this.updater = updater;
        }
    }
}
