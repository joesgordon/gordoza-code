package org.jutils.ui.net;

import java.net.*;
import java.util.Enumeration;
import java.util.List;

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
public class NetworkInterfaceField implements IDataFormField<String>
{
    /**  */
    private final StringFormField nicField;
    /**  */
    private final NicChoiceDescriptor descriptor;
    /**  */
    private final JPopupMenu nicMenu;

    /***************************************************************************
     * @param name
     **************************************************************************/
    public NetworkInterfaceField( String name )
    {
        this.descriptor = new NicChoiceDescriptor();
        this.nicField = new StringFormField( name );
        this.nicMenu = new JPopupMenu();

        buildNicMenu();

        nicField.getView().getComponent( 0 ).addMouseListener(
            new RightClickListener( ( e ) -> nicMenu.show( e.getComponent(),
                e.getX(), e.getY() ) ) );
    }

    /***************************************************************************
     * @param e
     **************************************************************************/
    private void buildNicMenu()
    {
        List<NicInfo> nics = NetUtils.buildNicList();

        nicMenu.removeAll();

        for( NicInfo nic : nics )
        {
            String title = nic.addressString + " : " + nic.name;
            JMenuItem item = new JMenuItem( title );
            item.addActionListener(
                ( e ) -> nicField.setValue( nic.address.getHostAddress() ) );
            nicMenu.add( item );
        }

        if( nics.isEmpty() )
        {
            JMenuItem item = new JMenuItem( "No NICs Detected" );
            item.setEnabled( false );
            nicMenu.add( item );
        }

        nicMenu.addSeparator();

        JMenuItem item = new JMenuItem( "Refresh",
            IconConstants.getIcon( IconConstants.REFRESH_16 ) );
        item.addActionListener( ( ae ) -> buildNicMenu() );
        nicMenu.add( item );
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
        return nicField.getValue();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setValue( String value )
    {
        nicField.setValue( value );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setUpdater( IUpdater<String> updater )
    {
        nicField.setUpdater( updater );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IUpdater<String> getUpdater()
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
    private static class NicChoiceDescriptor implements IDescriptor<NicChoice>
    {
        /**  */
        public boolean showIpv6;
        /**  */
        public boolean showIpv4;

        /**
         * {@inheritDoc}
         */
        @Override
        public String getDescription( NicChoice choice )
        {
            return choice == null ? "" : choice.getTitle( showIpv6, showIpv4 );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private final static class NicUpdater implements IUpdater<NicChoice>
    {
        /**  */
        private IUpdater<String> updater;

        /**
         * {@inheritDoc}
         */
        @Override
        public void update( NicChoice choice )
        {
            if( updater != null )
            {
                String name = choice.nic == null ? null : choice.nic.getName();
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
