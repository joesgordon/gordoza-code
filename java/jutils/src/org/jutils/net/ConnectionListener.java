package org.jutils.net;

import java.io.Closeable;
import java.io.IOException;

import org.jutils.concurrent.StoppableThread;
import org.jutils.ui.event.ItemActionListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ConnectionListener implements Closeable
{
    /**  */
    public final IConnection connection;
    /**  */
    private final StoppableThread rxThread;

    /***************************************************************************
     * @param socket
     * @param msgListener
     * @param errListener
     * @throws IOException
     **************************************************************************/
    public ConnectionListener( IConnection connection,
        ItemActionListener<NetMessage> msgListener,
        ItemActionListener<String> errListener ) throws IOException
    {
        this.connection = connection;

        ConnectionReceiver receiver = new ConnectionReceiver( connection );

        this.rxThread = new StoppableThread( receiver, "Connection Receiver" );

        receiver.addMessageListener( msgListener );
        receiver.addErrorListener( errListener );

        rxThread.start();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void close() throws IOException
    {
        rxThread.interrupt();
        rxThread.stopAndWait();
        connection.close();
    }
}
