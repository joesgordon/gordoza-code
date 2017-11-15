package org.jutils.net;

import java.io.*;
import java.net.*;
import java.util.Arrays;

import org.jutils.io.IOUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TcpConnection implements IConnection
{
    /**  */
    private final byte [] rxBuffer;
    /**  */
    private final Runnable disconnetCallback;

    /**  */
    private Socket socket;
    /**  */
    private InetAddress remoteAddress;
    /**  */
    private int remotePort;
    /**  */
    private BufferedInputStream input;
    /**  */
    private OutputStream output;

    /***************************************************************************
     * @param port
     * @throws IOException
     **************************************************************************/
    public TcpConnection( TcpInputs inputs, Runnable disconnetCallback )
        throws IOException
    {
        Socket socket = null;

        InetAddress nicAddr = IConnection.getNicAddress( inputs.nic );

        socket = new Socket( inputs.remoteAddress.getInetAddress(),
            inputs.remotePort, nicAddr, inputs.localPort );
        socket.setSoTimeout( inputs.timeout );

        this.disconnetCallback = disconnetCallback;
        this.socket = socket;
        this.rxBuffer = new byte[65535];

        setSocket( socket );
    }

    TcpConnection( Socket socket, Runnable disconnetCallback )
        throws IOException
    {
        socket.setSoTimeout( 1000 );

        this.disconnetCallback = disconnetCallback;
        this.socket = socket;
        this.rxBuffer = new byte[65535];

        setSocket( socket );
    }

    /***************************************************************************
     * @param socket
     * @throws IOException
     **************************************************************************/
    private void setSocket( Socket socket ) throws IOException
    {
        this.socket = socket;
        this.remoteAddress = socket.getInetAddress();
        this.remotePort = socket.getPort();
        this.input = new BufferedInputStream( socket.getInputStream(),
            IOUtils.DEFAULT_BUF_SIZE );
        this.output = socket.getOutputStream();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void close() throws IOException
    {
        if( socket != null )
        {
            socket.shutdownInput();
            socket.shutdownOutput();

            input.close();
            output.flush();
            output.close();
            socket.close();

            socket = null;
            output = null;
            input = null;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public NetMessage txMessage( byte [] contents ) throws IOException
    {
        try
        {
            socket.getOutputStream().write( contents );
        }
        catch( SocketTimeoutException ex )
        {
            disconnetCallback.run();
            return null;
        }

        return new NetMessage( false, remoteAddress.getHostAddress(),
            remotePort, contents );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public NetMessage rxMessage() throws IOException
    {
        int len = input.read( rxBuffer );

        if( len == -1 )
        {
            // connection closed?
            disconnetCallback.run();
            return null;
            // throw new SocketTimeoutException();
        }
        else if( len == 0 )
        {
            throw new SocketTimeoutException();
        }

        byte [] contents = Arrays.copyOf( rxBuffer, len );

        return new NetMessage( true, remoteAddress.getHostAddress(), remotePort,
            contents );

    }

    public Ip4Address getRemoteAddress()
    {
        Ip4Address address = new Ip4Address();

        address.set( socket.getInetAddress() );

        return address;
    }

    public int getRemotePort()
    {
        return socket.getPort();
    }

    public void setTimeout( int millis ) throws SocketException
    {
        socket.setSoTimeout( millis );
    }
}
