package org.mc.io;

import java.io.Closeable;
import java.io.IOException;
import java.net.*;
import java.util.*;

import org.mc.McMessage;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MulticastConnection implements Closeable
{
    /**  */
    private final InetAddress address;
    /**  */
    private final MulticastSocket socket;
    /**  */
    private final int port;
    /**  */
    private final byte[] rxBuffer;
    /**  */
    private final DatagramPacket rxPacket;
    /**  */
    private final List<InterfaceAddress> localHosts;

    /***************************************************************************
     * @param group
     * @param port
     * @param ttl
     * @param msgLength
     * @throws IOException
     **************************************************************************/
    public MulticastConnection( String group, int port, int ttl,
        NetworkInterface nic ) throws IOException
    {
        if( port < 1 || port > 65535 )
        {
            throw new IOException( "Invalid port: " + port );
        }
        if( ttl < 0 || ttl > 255 )
        {
            throw new IOException( "Invalid Time to Live: " + ttl );
        }

        address = InetAddress.getByName( group );
        rxBuffer = new byte[65535];
        socket = new MulticastSocket( port );
        rxPacket = new DatagramPacket( rxBuffer, rxBuffer.length, address,
            port );
        this.port = port;

        socket.setTimeToLive( ttl );
        socket.joinGroup( address );
        socket.setSoTimeout( 1000 );

        if( nic != null )
        {
            socket.setNetworkInterface( nic );
        }
        else
        {
            nic = socket.getNetworkInterface();
        }

        if( nic == null )
        {
            throw new IOException(
                "No NIC configured for " + group + ":" + port );
        }

        this.localHosts = nic.getInterfaceAddresses();
    }

    /***************************************************************************
     * @param msg
     * @param buffer
     **************************************************************************/
    private void fillMessage( McMessage msg, byte[] buffer,
        DatagramPacket packet )
    {
        msg.time = GregorianCalendar.getInstance().getTimeInMillis();
        msg.contents = buffer;
        msg.address = packet.getAddress().getHostAddress();
        msg.port = port;
        msg.selfMessage = false;

        InetAddress pa = packet.getAddress();
        for( InterfaceAddress ia : localHosts )
        {
            if( ia.getAddress().equals( pa ) )
            {
                msg.selfMessage = true;
            }
        }
    }

    /***************************************************************************
     * @param buf
     * @return
     * @throws IOException
     **************************************************************************/
    public McMessage txMessage( byte[] buf ) throws IOException
    {
        McMessage msg = new McMessage();

        // LogUtils.printDebug( "Sending message..." );

        DatagramPacket pack = new DatagramPacket( buf, buf.length, address,
            port );

        socket.send( pack );

        fillMessage( msg, buf, pack );

        return msg;
    }

    /***************************************************************************
     * @return
     * @throws IOException
     **************************************************************************/
    public McMessage rxMessage() throws IOException
    {
        McMessage msg = new McMessage();

        // LogUtils.printDebug( "Receiving message..." );

        socket.receive( rxPacket );

        fillMessage( msg, Arrays.copyOf( rxBuffer, rxPacket.getLength() ),
            rxPacket );

        return msg;
    }

    /***************************************************************************
     * @throws IOException
     **************************************************************************/
    @Override
    public void close() throws IOException
    {
        socket.leaveGroup( address );
        socket.close();
    }
}
