package org.jutils.ui.net;

import java.awt.event.MouseEvent;
import java.net.*;
import java.util.Enumeration;

import javax.swing.*;

import org.jutils.IconConstants;
import org.jutils.net.NetUtils;
import org.jutils.net.NetUtils.NicInfo;
import org.jutils.ui.event.RightClickListener;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.fields.*;
import org.jutils.ui.hex.HexUtils;
import org.jutils.ui.validation.IValidityChangedListener;
import org.jutils.ui.validation.Validity;
import org.jutils.utils.EnumerationIteratorAdapter;

/*******************************************************************************
 * 
 ******************************************************************************/
public class NetworkInterfaceComboField implements IDataFormField<String>
{
    /**  */
    private final ComboFormField<NicInfo> nicField;
    /**  */
    private final NicInfoDescriptor descriptor;
    /**  */
    private final NicUpdater updater;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public NetworkInterfaceComboField( String name )
    {
        this.descriptor = new NicInfoDescriptor();
        this.nicField = new ComboFormField<>( name, NetUtils.buildNicList(),
            descriptor );

        this.updater = new NicUpdater();

        nicField.setUpdater( updater );

        nicField.getView().getComponent( 0 ).addMouseListener(
            new RightClickListener( ( e ) -> showMenu( e ) ) );
    }

    /***************************************************************************
     * @param e
     **************************************************************************/
    private void showMenu( MouseEvent e )
    {
        JPopupMenu menu = new JPopupMenu();

        JMenuItem item = new JMenuItem( "Refresh",
            IconConstants.getIcon( IconConstants.REFRESH_16 ) );
        item.addActionListener(
            ( ae ) -> nicField.setValues( NetUtils.buildNicList() ) );
        menu.add( item );

        menu.show( e.getComponent(), e.getX(), e.getY() );
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
        NicInfo choice = nicField.getValue();

        return choice == null ? null : choice.name;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setValue( String value )
    {
        NicInfo info = null;

        if( value != null )
        {
            try
            {
                NetworkInterface nic = null;
                InetAddress address = null;

                nic = NetworkInterface.getByName( value );
                Enumeration<InetAddress> addresses = nic.getInetAddresses();

                if( addresses.hasMoreElements() )
                {
                    address = addresses.nextElement();
                    info = new NicInfo( nic, address );
                }
                else
                {
                    info = NicInfo.createAny();
                }
            }
            catch( SocketException ex )
            {
                info = NicInfo.createAny();
            }
        }
        else
        {
            info = NicInfo.createAny();
        }

        nicField.setValue( info );
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
    private static class NicChoice
    {
        /**  */
        private final NetworkInterface nic;
        /**  */
        private final String name;
        /**  */
        private final String v6Addr;
        /**  */
        private final String v4Addr;

        /**
         * @param nic
         */
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
            else
            {
                v4Addr = "0.0.0.0";
                v6Addr = "0000:0000:0000:0000:0000:0000:0000:0000";
            }

            this.nic = nic;
            this.name = nic == null ? "Any" : nic.getDisplayName();
            this.v6Addr = v6Addr;
            this.v4Addr = v4Addr;
        }

        /**
         * @param showIpv6
         * @param showIpv4
         * @return
         */
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

        /**
         * {@inheritDoc}
         */
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

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode()
        {
            return nic == null ? 0 : nic.hashCode();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class NicInfoDescriptor implements IDescriptor<NicInfo>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getDescription( NicInfo choice )
        {
            return choice.name + " : " + choice.addressString;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private final static class NicUpdater implements IUpdater<NicInfo>
    {
        /**  */
        private IUpdater<String> updater;

        /**
         * {@inheritDoc}
         */
        @Override
        public void update( NicInfo choice )
        {
            if( updater != null )
            {
                String name = choice.name;
                updater.update( name );
            }
        }

        /**
         * @return
         */
        public IUpdater<String> getUpdater()
        {
            return updater;
        }

        /**
         * @param updater
         */
        public void setUpdater( IUpdater<String> updater )
        {
            this.updater = updater;
        }
    }
}
