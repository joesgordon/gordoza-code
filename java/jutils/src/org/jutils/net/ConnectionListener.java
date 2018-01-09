package org.jutils.net;

import java.io.Closeable;
import java.io.IOException;

import org.jutils.concurrent.Stoppable;
import org.jutils.ui.event.ItemActionListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ConnectionListener implements Closeable
{
    /**  */
    public final IConnection connection;
    /**  */
    private final Stoppable rxTask;
    /**  */
    private final Thread rxThread;

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

        this.rxTask = new Stoppable( receiver );
        this.rxThread = new Thread( rxTask );

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
        rxTask.stop();
        connection.close();

        try
        {
            rxTask.stopAndWaitFor();
        }
        catch( InterruptedException e )
        {
        }
        finally
        {
        }
    }
}
