package org.jutils.net;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MulticastConnection implements IConnection
{
    /**  */
    private final InetAddress address;
    /**  */
    private final MulticastSocket socket;
    /**  */
    private final int port;
    /**  */
    private final byte [] rxBuffer;
    /**  */
    private final DatagramPacket rxPacket;

    /***************************************************************************
     * @param group
     * @param port
     * @param ttl
     * @param msgLength
     * @throws IOException
     **************************************************************************/
    public MulticastConnection( MulticastInputs socket ) throws IOException
    {
        if( socket.port < 1 || socket.port > 65535 )
        {
            throw new IOException( "Invalid port: " + socket.port );
        }

        if( socket.ttl < 0 || socket.ttl > 255 )
        {
            throw new IOException( "Invalid Time to Live: " + socket.ttl );
        }

        this.address = socket.address.getInetAddress();
        this.rxBuffer = new byte[2048];
        this.socket = new MulticastSocket( socket.port );
        this.rxPacket = new DatagramPacket( rxBuffer, rxBuffer.length, address,
            socket.port );
        this.port = socket.port;

        this.socket.setLoopbackMode( !socket.loopback );
        this.socket.setTimeToLive( socket.ttl );
        this.socket.joinGroup( address );
        this.socket.setSoTimeout( socket.timeout );

        if( socket.nic != null )
        {
            NetworkInterface nic = NetworkInterface.getByName( socket.nic );

            if( nic != null )
            {
                this.socket.setNetworkInterface( nic );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public NetMessage txMessage( byte [] buf ) throws IOException
    {
        // LogUtils.printDebug( "Sending message..." );

        DatagramPacket pack = new DatagramPacket( buf, buf.length, address,
            port );

        socket.send( pack );

        NetMessage msg = new NetMessage( false, address.getHostAddress(), port,
            buf );

        return msg;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public NetMessage rxMessage() throws IOException
    {
        // LogUtils.printDebug( "Receiving message..." );

        socket.receive( rxPacket );

        byte [] contents = Arrays.copyOf( rxBuffer, rxPacket.getLength() );
        InetAddress address = rxPacket.getAddress();
        int port = rxPacket.getPort();

        NetMessage msg = new NetMessage( true, address.getHostAddress(), port,
            contents );

        return msg;
    }

    /***************************************************************************
     * @throws IOException
     **************************************************************************/
    @Override
    public void close() throws IOException
    {
        socket.close();
    }
}
