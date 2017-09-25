package org.jutils.net;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jutils.io.IOUtils;
import org.jutils.ui.event.updater.IUpdater;

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
    private final ITcpHandler handler;
    /**  */
    private final AtomicBoolean continueRx;

    /**  */
    private Socket socket;
    /**  */
    private InetAddress clientAddress;
    /**  */
    private int clientPort;
    /**  */
    private BufferedInputStream input;
    /**  */
    private OutputStream output;

    /***************************************************************************
     * @param port
     * @throws IOException
     **************************************************************************/
    public TcpConnection( int port, ITcpHandler handler ) throws IOException
    {
        this( port, 500, handler );
    }

    /***************************************************************************
     * @param port
     * @param timeout
     * @param handler
     * @throws IOException
     **************************************************************************/
    public TcpConnection( int port, int timeout, ITcpHandler handler )
        throws IOException
    {
        this.handler = handler;
        this.server = new ServerSocket( port );
        this.rxBuffer = new byte[65535];
        this.continueRx = new AtomicBoolean( false );

        server.setSoTimeout( timeout );
    }

    /***************************************************************************
     * @param port
     * @throws IOException
     **************************************************************************/
    public TcpConnection( TcpClientInputs inputs, ITcpHandler handler )
        throws IOException
    {
        this.handler = handler;
        this.server = null;
        this.socket = new Socket( inputs.address.getInetAddress(),
            inputs.port );
        this.rxBuffer = new byte[65535];
        this.continueRx = new AtomicBoolean( false );

        server.setSoTimeout( inputs.timeout );
    }

    /***************************************************************************
     * @return
     * @throws IOException
     **************************************************************************/
    public boolean accept() throws IOException
    {
        if( this.socket == null )
        {
            Socket socket;

            try
            {
                socket = server.accept();

                this.socket = socket;
                this.clientAddress = socket.getInetAddress();
                this.clientPort = socket.getPort();
                this.input = new BufferedInputStream( socket.getInputStream(),
                    IOUtils.DEFAULT_BUF_SIZE );
                this.output = socket.getOutputStream();
                this.continueRx.set( true );

                Thread t = new Thread( () -> runReceive(), "TCP Rx Thread" );

                t.start();
            }
            catch( SocketTimeoutException ex )
            {
                return false;
            }
        }

        return true;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void close() throws IOException
    {
        continueRx.set( false );

        if( socket != null )
        {
            input.close();
            output.close();
            socket.close();

            socket = null;
            output = null;
            input = null;
        }

        server.close();
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
            handler.signalDisconnected();
            return null;
        }

        return new NetMessage( contents, clientAddress, clientPort );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void runReceive()
    {
        while( continueRx.get() )
        {
            try
            {
                if( rxMessage() == null )
                {
                    break;
                }
            }
            catch( SocketTimeoutException ex )
            {
                continue;
            }
            catch( IOException ex )
            {
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public NetMessage rxMessage() throws IOException
    {
        try( InputStream stream = socket.getInputStream() )
        {
            int len = stream.read( rxBuffer );

            if( len == -1 )
            {
                // connection closed.
                handler.signalDisconnected();
                return null;
            }
            else if( len == 0 )
            {
                throw new SocketTimeoutException();
            }

            byte [] contents = Arrays.copyOf( rxBuffer, len );

            handler.signalReceived( contents );

            return new NetMessage( Arrays.copyOf( contents, len ),
                clientAddress, clientPort );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static interface ITcpHandler
    {
        /***********************************************************************
         * 
         **********************************************************************/
        public void signalDisconnected();

        /***********************************************************************
         * @param data
         **********************************************************************/
        public void signalReceived( byte [] data );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static class TcpHandlerFuncs implements ITcpHandler
    {
        private final Runnable disconnected;
        private final IUpdater<byte []> rxd;

        public TcpHandlerFuncs( Runnable disconnected, IUpdater<byte []> rxd )
        {
            this.disconnected = disconnected;
            this.rxd = rxd;
        }

        @Override
        public void signalDisconnected()
        {
            disconnected.run();
        }

        @Override
        public void signalReceived( byte [] data )
        {
            rxd.update( data );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public static class TcpClientInputs
    {
        /**
         * The number of milliseconds to block for communications. Must be > -1.
         * 0 is interpreted as an infinite timeout.
         */
        public int timeout;
        /**  */
        public Ip4Address address;
        /**  */
        public int port;

        public TcpClientInputs()
        {
            this.timeout = 0;
            this.address = new Ip4Address( 127, 0, 0, 1 );
            this.port = 80;
        }
    }
}
