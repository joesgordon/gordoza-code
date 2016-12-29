package org.mc.io;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

import org.mc.McMessage;

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
    private final byte[] rxBuffer;
    /**  */
    private final DatagramPacket rxPacket;

    /***************************************************************************
     * @param group
     * @param port
     * @param ttl
     * @param msgLength
     * @throws IOException
     **************************************************************************/
    public MulticastConnection( MulticastSocketDef socket ) throws IOException
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
        this.rxBuffer = new byte[65535];
        this.socket = new MulticastSocket( socket.port );
        this.rxPacket = new DatagramPacket( rxBuffer, rxBuffer.length, address,
            socket.port );
        this.port = socket.port;

        this.socket.setReuseAddress( true );
        this.socket.setTimeToLive( socket.ttl );
        this.socket.joinGroup( address );
        this.socket.setSoTimeout( 1000 );

        NetworkInterface nic = socket.getSystemNic();

        if( nic != null )
        {
            this.socket.setNetworkInterface( nic );
        }
        // else
        // {
        // InetAddress ina = this.socket.getInterface();
        //
        // nic = NetworkInterface.getByName( ina.getHostName() );
        //
        // if( nic == null )
        // {
        // throw new IOException( "No NIC configured for " +
        // socket.address.toString() + ":" + port );
        // }
        // }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public McMessage txMessage( byte[] buf ) throws IOException
    {
        // LogUtils.printDebug( "Sending message..." );

        DatagramPacket pack = new DatagramPacket( buf, buf.length, address,
            port );

        socket.send( pack );

        McMessage msg = fillMessage( buf, pack );

        return msg;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public McMessage rxMessage() throws IOException
    {
        // LogUtils.printDebug( "Receiving message..." );

        socket.receive( rxPacket );

        byte[] contents = Arrays.copyOf( rxBuffer, rxPacket.getLength() );

        McMessage msg = fillMessage( contents, rxPacket );

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

    /***************************************************************************
     * @param msg
     * @param buffer
     **************************************************************************/
    private McMessage fillMessage( byte[] buffer, DatagramPacket packet )
    {
        return new McMessage( packet.getAddress().getHostAddress(), port,
            buffer );
    }
}
