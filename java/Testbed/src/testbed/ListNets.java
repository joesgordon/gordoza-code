package testbed;

import java.io.IOException;
import java.net.*;
import java.util.Collections;
import java.util.Enumeration;

import org.jutils.Utils;

public class ListNets
{
    private static final String TEST_ADDRESS = "225.26.45.1";
    private static final int TEST_PORT = 51200;

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
            Utils.arrayToString( nic.getHardwareAddress() ) );
        System.out.printf( "\n" );
    }

    @SuppressWarnings( "unused")
    private static void testNic( NetworkInterface nic ) throws IOException
    {
        MulticastSocket socket = new MulticastSocket( TEST_PORT );
        socket.setNetworkInterface( nic );
        // socket.setInterface( nic.getInterfaceAddresses().get( 0
        // ).getAddress() );
        socket.close();
    }

    @SuppressWarnings( "unused")
    private static void testSocket( MulticastSocket socket )
        throws SocketException, UnknownHostException, IOException
    {
        InetAddress group = InetAddress.getByName( TEST_ADDRESS );
        String message = "nic '" +
            socket.getNetworkInterface().getDisplayName() + "' on port '" +
            socket.getPort() + "'";
        socket.joinGroup( group );

        InetSocketAddress sendAddress = new InetSocketAddress( group, TEST_PORT );
        byte[] bytes = message.getBytes();
        DatagramPacket packet = new DatagramPacket( bytes, bytes.length,
            sendAddress );
        socket.send( packet );
    }
}
