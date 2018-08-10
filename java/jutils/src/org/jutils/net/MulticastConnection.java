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
     * @param inputs the configuration values for this connection.
     * @throws IOException if any error occurs binding to the socket using the
     * provided configuration values.
     **************************************************************************/
    public MulticastConnection( MulticastInputs inputs ) throws IOException
    {
        if( inputs.port < 1 || inputs.port > 65535 )
        {
            throw new IOException( "Invalid port: " + inputs.port );
        }

        if( inputs.ttl < 0 || inputs.ttl > 255 )
        {
            throw new IOException( "Invalid Time to Live: " + inputs.ttl );
        }

        this.address = inputs.group.getInetAddress();
        this.rxBuffer = new byte[2048];
        this.socket = new MulticastSocket( inputs.port );
        this.rxPacket = new DatagramPacket( rxBuffer, rxBuffer.length, address,
            inputs.port );
        this.port = inputs.port;

        this.socket.setLoopbackMode( !inputs.loopback );
        this.socket.setTimeToLive( inputs.ttl );
        this.socket.setSoTimeout( inputs.timeout );

        if( inputs.nic != null )
        {
            NetworkInterface nic = NetworkInterface.getByName( inputs.nic );

            if( nic != null )
            {
                this.socket.setNetworkInterface( nic );
            }
        }

        this.socket.joinGroup( address );
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

        String localAddress = socket.getLocalAddress().getHostAddress();
        int localPort = socket.getLocalPort();

        String remoteAddress = address.getHostAddress();
        int remotePort = port;

        NetMessage msg = new NetMessage( false, localAddress, localPort,
            remoteAddress, remotePort, buf );

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

        String localAddress = socket.getLocalAddress().getHostAddress();
        int localPort = socket.getLocalPort();

        String remoteAddress = address.getHostAddress();
        int remotePort = port;

        NetMessage msg = new NetMessage( true, localAddress, localPort,
            remoteAddress, remotePort, contents );

        return msg;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void close() throws IOException
    {
        socket.close();
    }
}
