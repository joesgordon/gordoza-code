package org.jutils.net;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UdpConnection implements IConnection
{
    /**  */
    private final DatagramSocket socket;
    /**  */
    private final byte [] rxBuffer;

    /**  */
    public InetAddress remoteAddress;
    /**  */
    public int remotePort;

    /***************************************************************************
     * @param inputs
     * @throws IOException
     * @throws SocketException
     **************************************************************************/
    public UdpConnection( UdpInputs inputs ) throws IOException, SocketException
    {
        DatagramSocket socket;
        InetAddress nicAddr = IConnection.getNicAddress( inputs.nic );

        // LogUtils.printDebug( "NIC: " + inputs.nic );
        // LogUtils.printDebug( "Address: " + nicAddr );
        // LogUtils.printDebug( "port: " + inputs.port );
        // LogUtils.printDebug( "" );

        if( inputs.reuse )
        {
            socket = new DatagramSocket( null );
            socket.setReuseAddress( true );
            socket.bind( new InetSocketAddress( nicAddr, inputs.localPort ) );
        }
        else
        {
            socket = new DatagramSocket( inputs.localPort, nicAddr );
        }

        socket.setSoTimeout( inputs.timeout );

        this.socket = socket;
        this.rxBuffer = new byte[65535];

        if( inputs.remoteAddress != null )
        {
            this.remoteAddress = inputs.remoteAddress.getInetAddress();
        }
        this.remotePort = inputs.remotePort;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void close() throws IOException
    {
        socket.close();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public NetMessage rxMessage() throws IOException, SocketTimeoutException
    {
        DatagramPacket packet = new DatagramPacket( rxBuffer, rxBuffer.length );
        socket.receive( packet );
        byte [] contents = Arrays.copyOf( rxBuffer, packet.getLength() );
        InetAddress address = packet.getAddress();
        int port = packet.getPort();
        return new NetMessage( true, socket.getLocalAddress().getHostAddress(),
            socket.getLocalPort(), address.getHostAddress(), port, contents );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public NetMessage txMessage( byte [] contents ) throws IOException
    {
        if( remoteAddress == null )
        {
            throw new RuntimeException( "No remote address specified" );
        }

        return txMessage( contents, remoteAddress, remotePort );
    }

    /***************************************************************************
     * @param contents
     * @param toAddr
     * @param toPort
     * @return
     * @throws IOException
     **************************************************************************/
    public NetMessage txMessage( byte [] contents, InetAddress toAddr,
        int toPort ) throws IOException
    {
        DatagramPacket packet = new DatagramPacket( contents, contents.length,
            toAddr, toPort );

        socket.send( packet );

        return new NetMessage( false, socket.getLocalAddress().getHostAddress(),
            socket.getLocalPort(), toAddr.getHostAddress(), toPort, contents );
    }
}
