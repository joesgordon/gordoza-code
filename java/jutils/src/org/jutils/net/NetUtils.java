package org.jutils.net;

import java.net.*;
import java.util.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class NetUtils
{
    /***************************************************************************
     * 
     **************************************************************************/
    private NetUtils()
    {
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static List<NicInfo> buildNicList()
    {
        List<NicInfo> nics = new ArrayList<>();
        Enumeration<NetworkInterface> nets;

        nics.add( NicInfo.createAny() );

        try
        {
            nets = NetworkInterface.getNetworkInterfaces();

            for( NetworkInterface nic : Collections.list( nets ) )
            {
                if( nic.isUp() )
                {
                    Enumeration<InetAddress> addresses = nic.getInetAddresses();
                    while( addresses.hasMoreElements() )
                    {
                        InetAddress address = addresses.nextElement();
                        nics.add( new NicInfo( nic, address ) );
                    }
                }
            }
        }
        catch( SocketException ex )
        {
            throw new RuntimeException( ex.getMessage(), ex );
        }

        return nics;
    }

    /***************************************************************************
     * @param nicString
     * @return
     **************************************************************************/
    public static NetworkInterface lookupNic( String nicString )
    {
        List<NicInfo> nics = buildNicList();
        NetworkInterface nic = null;

        for( NicInfo info : nics )
        {
            if( info.name.toLowerCase().equals( nicString.toLowerCase() ) )
            {
                return info.nic;
            }
            else if( info.addressString.equals( nicString ) )
            {
                return info.nic;
            }
        }

        return nic;
    }

    /***************************************************************************
     *
     **************************************************************************/
    public static class NicInfo
    {
        /**  */
        public final NetworkInterface nic;
        /**  */
        public final InetAddress address;

        /**  */
        public final String name;
        /**  */
        public final String addressString;
        /**  */
        public final boolean isIpv4;

        /**
         * @param nic {@code null} indicates Any.
         * @param address the address of the provided NIC may not be null.
         */
        public NicInfo( NetworkInterface nic, InetAddress address )
        {
            this.nic = nic;
            this.name = nic != null ? nic.getDisplayName() : "Any";
            this.address = address;
            this.addressString = address.getHostAddress();
            this.isIpv4 = address instanceof Inet4Address;
        }

        /**
         * @return
         */
        public static NicInfo createAny()
        {
            try
            {
                byte [] bytes = new byte[] { 0, 0, 0, 0 };
                InetAddress address = InetAddress.getByAddress( bytes );

                return new NicInfo( null, address );
            }
            catch( UnknownHostException ex )
            {
                throw new RuntimeException(
                    "0.0.0.0 was identified as a bad address" );
            }
        }
    }
}
