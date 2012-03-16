package org.mc;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.GregorianCalendar;

/*******************************************************************************
 * 
 ******************************************************************************/
public class McComm
{
    /**  */
    private final InetAddress address;
    /**  */
    private final MulticastSocket socket;
    /**  */
    private final byte[] rxBuffer;
    /**  */
    private final DatagramPacket rxPacket;
    /**  */
    private final int port;
    /**  */
    private final InetAddress localHost;

    /***************************************************************************
     * @param group
     * @param port
     * @param ttl
     * @param msgLength
     * @throws IOException
     **************************************************************************/
    public McComm( String group, int port, int ttl, int msgLength,
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
        rxBuffer = new byte[msgLength];
        socket = new MulticastSocket( port );
        rxPacket = new DatagramPacket( rxBuffer, rxBuffer.length, address, port );
        this.port = port;

        socket.setNetworkInterface( nic );
        socket.setTimeToLive( ttl );
        socket.joinGroup( address );
        socket.setSoTimeout( 1000 );

        localHost = InetAddress.getLocalHost();
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
        msg.selfMessage = packet.getAddress().equals( localHost );
    }

    /***************************************************************************
     * @param buf
     * @return
     * @throws IOException
     **************************************************************************/
    public McMessage txMessage( byte[] buf ) throws IOException
    {
        McMessage msg = new McMessage();

        // System.out.println( "Sending message..." );

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

        // System.out.println( "Receiving message..." );

        socket.receive( rxPacket );

        fillMessage( msg, Arrays.copyOf( rxBuffer, rxPacket.getLength() ),
            rxPacket );

        return msg;
    }

    /***************************************************************************
     * @throws IOException
     **************************************************************************/
    public void close() throws IOException
    {
        socket.leaveGroup( address );
        socket.close();
    }
}
