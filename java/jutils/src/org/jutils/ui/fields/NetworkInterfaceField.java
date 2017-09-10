package org.jutils.ui.fields;

import java.awt.event.MouseEvent;
import java.net.*;
import java.util.*;

import javax.swing.*;

import org.jutils.ui.event.RightClickListener;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.hex.HexUtils;
import org.jutils.ui.validation.IValidityChangedListener;
import org.jutils.ui.validation.Validity;
import org.jutils.utils.EnumerationIteratorAdapter;

/*******************************************************************************
 * 
 ******************************************************************************/
public class NetworkInterfaceField implements IDataFormField<String>
{
    /**  */
    private final ComboFormField<NicChoice> nicField;
    /**  */
    private final NicChoiceDescriptor descriptor;
    /**  */
    private final NicUpdater updater;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public NetworkInterfaceField( String name )
    {
        this.descriptor = new NicChoiceDescriptor();
        this.nicField = new ComboFormField<>( name, buildNicList(),
            descriptor );

        this.updater = new NicUpdater();

        nicField.setUpdater( updater );

        nicField.getView().getComponent( 0 ).addMouseListener(
            new RightClickListener( ( e ) -> showMenu( e ) ) );
    }

    private void showMenu( MouseEvent e )
    {
        JPopupMenu menu = new JPopupMenu();

        JCheckBoxMenuItem itemv6 = new JCheckBoxMenuItem( "Show IPv6",
            descriptor.showIpv6 );
        itemv6.addActionListener(
            ( ae ) -> descriptor.showIpv6 = itemv6.isSelected() );
        menu.add( itemv6 );

        JCheckBoxMenuItem itemv4 = new JCheckBoxMenuItem( "Show IPv4",
            descriptor.showIpv4 );
        itemv4.addActionListener(
            ( ae ) -> descriptor.showIpv4 = itemv4.isSelected() );
        menu.add( itemv4 );

        menu.show( e.getComponent(), e.getX(), e.getY() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private static List<NicChoice> buildNicList()
    {
        List<NicChoice> nics = new ArrayList<>();
        Enumeration<NetworkInterface> nets;

        nics.add( new NicChoice( null ) );

        try
        {
            nets = NetworkInterface.getNetworkInterfaces();

            for( NetworkInterface nic : Collections.list( nets ) )
            {
                Enumeration<InetAddress> inetAddresses = nic.getInetAddresses();
                if( inetAddresses.hasMoreElements() )
                {
                    nics.add( new NicChoice( nic ) );
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
        NicChoice choice = nicField.getValue();

        return choice == null ? null : choice.nic.getName();
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

        nicField.setValue( new NicChoice( nic ) );
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

    private static class NicChoice
    {
        private final NetworkInterface nic;
        private final String name;
        private final String v6Addr;
        private final String v4Addr;

        public NicChoice( NetworkInterface nic )
        {
            String v6Addr = "N/A";
            String v4Addr = "N/A";

            if( nic != null )
            {
                Enumeration<InetAddress> addresses = nic.getInetAddresses();

                for( InetAddress address : new EnumerationIteratorAdapter<>(
                    addresses ) )
                {
                    if( address instanceof Inet4Address )
                    {
                        v4Addr = address.getHostAddress();
                    }
                    else if( address instanceof Inet6Address )
                    {
                        Inet6Address ina6 = ( Inet6Address )address;
                        byte [] bytes = ina6.getAddress();
                        v6Addr = "";

                        for( int i = 0; i < bytes.length; i += 2 )
                        {
                            if( i > 0 )
                            {
                                v6Addr += ":";
                            }
                            v6Addr += HexUtils.toHexString( bytes[i] );
                            v6Addr += HexUtils.toHexString( bytes[i + 1] );
                        }
                    }
                }
            }

            this.nic = nic;
            this.name = nic == null ? "<< Use Routing Table >>"
                : nic.getDisplayName();
            this.v6Addr = v6Addr;
            this.v4Addr = v4Addr;
        }

        public String getTitle( boolean showIpv6, boolean showIpv4 )
        {
            String title = name;

            if( showIpv6 && showIpv4 )
            {
                title = String.format( "%s [%s | %s]", name, v6Addr, v4Addr );
            }
            else if( showIpv6 )
            {
                title = String.format( "%s [%s]", name, v6Addr );
            }
            else if( showIpv4 )
            {
                title = String.format( "%s [%s]", name, v4Addr );
            }

            return title;
        }

        @Override
        public boolean equals( Object obj )
        {
            if( obj == this )
            {
                return true;
            }
            else if( obj == null || !( obj instanceof NicChoice ) )
            {
                return false;
            }

            NicChoice nc = ( NicChoice )obj;

            if( nic == nc.nic )
            {
                return true;
            }
            else if( nic != null )
            {
                return nic.equals( nc.nic );
            }

            return false;
        }

        @Override
        public int hashCode()
        {
            return nic == null ? 0 : nic.hashCode();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class NicChoiceDescriptor implements IDescriptor<NicChoice>
    {
        public boolean showIpv6;
        public boolean showIpv4;

        @Override
        public String getDescription( NicChoice choice )
        {
            return choice.getTitle( showIpv6, showIpv4 );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private final static class NicUpdater implements IUpdater<NicChoice>
    {
        private IUpdater<String> updater;

        @Override
        public void update( NicChoice choice )
        {
            if( updater != null )
            {
                String name = choice.nic == null ? null : choice.nic.getName();
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
