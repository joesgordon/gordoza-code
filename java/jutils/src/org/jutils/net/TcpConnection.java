package org.jutils.net;

import java.io.*;
import java.net.*;
import java.util.*;

import org.jutils.io.IOUtils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TcpConnection implements IConnection
{
    /**  */
    private final Socket socket;
    /**  */
    private final byte [] rxBuffer;
    /**  */
    private final List<Runnable> disconnetListeners;

    /**  */
    private final InetAddress remoteAddress;
    /**  */
    private final int remotePort;
    /**  */
    private final BufferedInputStream input;
    /**  */
    private final OutputStream output;

    /***************************************************************************
     * @param inputs
     * @param disconnetCallback
     * @throws IOException
     **************************************************************************/
    public TcpConnection( TcpInputs inputs ) throws IOException
    {
        this( createSocket( inputs ), inputs.timeout );
    }

    /***************************************************************************
     * @param socket
     * @param disconnetCallback
     * @throws IOException
     **************************************************************************/
    TcpConnection( Socket socket ) throws IOException
    {
        this( socket, 1000 );
    }

    /***************************************************************************
     * @param socket
     * @param timeout
     * @throws IOException
     **************************************************************************/
    private TcpConnection( Socket socket, int timeout ) throws IOException
    {
        this.socket = socket;
        this.rxBuffer = new byte[65535];
        this.disconnetListeners = new ArrayList<>();

        this.remoteAddress = socket.getInetAddress();
        this.remotePort = socket.getPort();
        this.input = new BufferedInputStream( socket.getInputStream(),
            IOUtils.DEFAULT_BUF_SIZE );
        this.output = socket.getOutputStream();

        socket.setSoTimeout( timeout );
    }

    /***************************************************************************
     * @param inputs
     * @return
     * @throws UnknownHostException
     * @throws IOException
     **************************************************************************/
    private static Socket createSocket( TcpInputs inputs )
        throws UnknownHostException, IOException
    {
        Socket socket = null;

        InetAddress nicAddr = IConnection.getNicAddress( inputs.nic );

        socket = new Socket( inputs.remoteAddress.getInetAddress(),
            inputs.remotePort, nicAddr, inputs.localPort );
        socket.setSoTimeout( inputs.timeout );

        return socket;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void fireDisconnected()
    {
        for( Runnable listener : disconnetListeners )
        {
            listener.run();
        }
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
        }
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public NetMessage sendMessage( byte [] contents ) throws IOException
    {
        try
        {
            socket.getOutputStream().write( contents );
        }
        catch( SocketTimeoutException ex )
        {
            fireDisconnected();
            return null;
        }

        return new NetMessage( false, socket.getLocalAddress().getHostAddress(),
            socket.getLocalPort(), remoteAddress.getHostAddress(), remotePort,
            contents );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public NetMessage receiveMessage() throws IOException
    {
        int len = input.read( rxBuffer );

        if( len == -1 )
        {
            // connection closed?
            fireDisconnected();
            return null;
            // throw new SocketTimeoutException();
        }
        else if( len == 0 )
        {
            throw new SocketTimeoutException();
        }

        byte [] contents = Arrays.copyOf( rxBuffer, len );

        return new NetMessage( true, socket.getLocalAddress().getHostAddress(),
            socket.getLocalPort(), remoteAddress.getHostAddress(), remotePort,
            contents );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void addDisconnectedListener( Runnable listener )
    {
        disconnetListeners.add( listener );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Ip4Address getRemoteAddress()
    {
        return new Ip4Address( remoteAddress );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getRemotePort()
    {
        return remotePort;
    }

    /***************************************************************************
     * @param millis
     * @throws SocketException
     **************************************************************************/
    public void setTimeout( int millis ) throws SocketException
    {
        socket.setSoTimeout( millis );
    }
}
