package org.jutils.net;

import java.io.Closeable;
import java.io.IOException;

import org.jutils.concurrent.Stoppable;
import org.jutils.ui.event.ItemActionListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Multicaster implements Closeable
{
    /**  */
    private final MulticastConnection connection;
    /**  */
    private final ConnectionReceiver receiver;
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
    public Multicaster( MulticastInputs socket,
        ItemActionListener<NetMessage> msgListener,
        ItemActionListener<String> errListener ) throws IOException
    {
        this.connection = new MulticastConnection( socket );
        this.receiver = new ConnectionReceiver( connection );
        this.rxTask = new Stoppable( receiver );
        this.rxThread = new Thread( rxTask );

        receiver.addMessageListener( msgListener );
        receiver.addErrorListener( errListener );

        rxThread.start();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public IConnection getConnection()
    {
        return connection;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void close() throws IOException
    {
        rxTask.stop();
        rxThread.interrupt();
        try
        {
            rxTask.stopAndWaitFor();
        }
        catch( InterruptedException e )
        {
        }
        connection.close();
    }
}
