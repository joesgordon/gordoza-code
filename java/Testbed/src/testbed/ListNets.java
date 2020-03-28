package testbed;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;

import org.jutils.core.ui.hex.HexUtils;

/**
 * 
 */
public class ListNets
{
    /**
     * @param args
     * @throws SocketException
     */
    public static void main( String args[] ) throws SocketException
    {
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        for( NetworkInterface nic : Collections.list( nets ) )
        {
            Enumeration<InetAddress> inetAddresses = nic.getInetAddresses();
            if( inetAddresses.hasMoreElements() )
            {
                displayInterfaceInformation( nic );
            }
        }
    }

    /**
     * @param nic
     * @throws SocketException
     */
    private static void displayInterfaceInformation( NetworkInterface nic )
        throws SocketException
    {
        System.out.printf( "Display name: %s\n", nic.getDisplayName() );
        System.out.printf( "Name: %s\n", nic.getName() );
        Enumeration<InetAddress> inetAddresses = nic.getInetAddresses();
        for( InetAddress inetAddress : Collections.list( inetAddresses ) )
        {
            System.out.printf( "InetAddress: %s\n", inetAddress );
        }
        System.out.printf( "Hardware Address: %s\n",
            HexUtils.toHexString( nic.getHardwareAddress() ) );
        System.out.printf( "\n" );
    }
}
