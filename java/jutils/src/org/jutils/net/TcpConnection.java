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
    private final ServerSocket server;
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
        ServerSocket server = null;
        Socket socket = null;

        InetAddress nicAddr = IConnection.getNicAddress( inputs.nic );

        if( inputs.isServer )
        {
            server = new ServerSocket( inputs.localPort, 64, nicAddr );
            server.setSoTimeout( inputs.timeout );
        }
        else
        {
            socket = new Socket( inputs.remoteAddress.getInetAddress(),
                inputs.remotePort, nicAddr, inputs.localPort );
            socket.setSoTimeout( inputs.timeout );
            setSocket( socket );
        }

        this.disconnetCallback = disconnetCallback;
        this.server = server;
        this.socket = socket;
        this.rxBuffer = new byte[65535];
    }

    /***************************************************************************
     * @return
     * @throws IOException
     **************************************************************************/
    public boolean accept() throws IOException
    {
        if( this.socket == null )
        {
            try
            {
                setSocket( server.accept() );
            }
            catch( SocketTimeoutException ex )
            {
                return false;
            }
        }

        return true;
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
            input.close();
            output.close();
            socket.close();

            socket = null;
            output = null;
            input = null;
        }

        if( server != null )
        {
            server.close();
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

        return new NetMessage( contents, remoteAddress, remotePort );
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
            // disconnetCallback.run();
            // return null;
            throw new SocketTimeoutException();
        }
        else if( len == 0 )
        {
            throw new SocketTimeoutException();
        }

        byte [] contents = Arrays.copyOf( rxBuffer, len );

        return new NetMessage( Arrays.copyOf( contents, len ), remoteAddress,
            remotePort );

    }
}
