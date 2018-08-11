package org.jutils.net;

import java.io.Closeable;
import java.io.IOException;

import org.jutils.concurrent.TaskThread;
import org.jutils.ui.event.ItemActionListener;

/*******************************************************************************
 * Creates a thread to listen for messages received with the provided
 * connection.
 ******************************************************************************/
public class ConnectionListener implements Closeable
{
    /** The connection used to send/receive messages. */
    public final IConnection connection;
    /** The receive thread. */
    private final TaskThread rxThread;

    /***************************************************************************
     * @param connection
     * @param msgListener
     * @param errListener
     * @throws IOException
     **************************************************************************/
    public ConnectionListener( IConnection connection,
        ItemActionListener<NetMessage> msgListener,
        ItemActionListener<String> errListener ) throws IOException
    {
        this.connection = connection;

        ConnectionReceiverTask receiver = new ConnectionReceiverTask(
            connection );

        this.rxThread = new TaskThread( receiver, "Connection Receiver" );

        receiver.addMessageListener( msgListener );
        receiver.addErrorListener( errListener );

        rxThread.start();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void close() throws IOException
    {
        rxThread.interrupt();
        rxThread.stopAndWait();
        connection.close();
    }
}
