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
     * @param inputs
     * @throws IOException
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

        this.address = inputs.address.getInetAddress();
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
