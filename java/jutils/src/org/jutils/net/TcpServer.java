package org.jutils.net;

import java.io.Closeable;
import java.io.IOException;
import java.net.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TcpServer implements Closeable
{
    /**  */
    private final ServerSocket server;

    /***************************************************************************
     * @param inputs
     * @throws IOException
     **************************************************************************/
    public TcpServer( TcpInputs inputs ) throws IOException
    {
        InetAddress nicAddr = IConnection.getNicAddress( inputs.nic );

        this.server = new ServerSocket( inputs.localPort, 64, nicAddr );

        server.setSoTimeout( inputs.timeout );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void close() throws IOException
    {
        server.close();
    }

    /***************************************************************************
     * @param disconnetCallback
     * @return
     * @throws IOException
     **************************************************************************/
    @SuppressWarnings( "resource")
    public TcpConnection accept( Runnable disconnetCallback )
        throws IOException, SocketTimeoutException
    {
        Socket socket = server.accept();

        TcpConnection connection = new TcpConnection( socket,
            disconnetCallback );

        return connection;
    }
}
