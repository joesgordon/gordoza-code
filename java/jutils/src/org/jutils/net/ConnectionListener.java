package org.jutils.net;

import java.io.Closeable;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.jutils.concurrent.ITaskHandler;
import org.jutils.concurrent.TaskThread;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.event.updater.UpdaterList;

/*******************************************************************************
 * Creates a thread to listen for messages received with the provided
 * connection.
 ******************************************************************************/
public class ConnectionListener implements Closeable
{
    /** The connection used to send/receive messages. */
    private IConnection connection;
    /** The receive thread. */
    private TaskThread rxThread;
    /**  */
    private final UpdaterList<NetMessage> msgListeners;
    /**  */
    private final UpdaterList<String> errListeners;
    /**  */
    private final UpdaterList<SocketTimeoutException> timeoutListeners;

    /***************************************************************************
     * @param connection
     * @param msgListener
     * @param errListener
     * @throws IOException
     **************************************************************************/
    public ConnectionListener()
    {
        this.connection = null;
        this.msgListeners = new UpdaterList<>();
        this.errListeners = new UpdaterList<>();
        this.timeoutListeners = new UpdaterList<>();
    }

    public void start( IConnection connection )
    {
        this.connection = connection;
        this.rxThread = new TaskThread( ( h ) -> run( h ),
            "Connection Receiver" );

        rxThread.start();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void close() throws IOException
    {
        if( connection != null )
        {
            rxThread.interrupt();
            rxThread.stopAndWait();
            connection.close();
        }

        connection = null;
        rxThread = null;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    public void run( ITaskHandler handler )
    {
        while( handler.canContinue() )
        {
            try
            {
                // LogUtils.printDebug( "Receiving message..." );
                NetMessage msg = connection.receiveMessage();
                if( msg == null )
                {
                    break;
                }
                msgListeners.fireListeners( msg );
            }
            catch( SocketTimeoutException ex )
            {
                // LogUtils.printDebug( "Receive timed out..." );
                timeoutListeners.fireListeners( ex );
            }
            catch( SocketException ex )
            {
                // LogUtils.printDebug( "Receive had an exception:" +
                // ex.getMessage() );
            }
            catch( IOException ex )
            {
                ex.printStackTrace();
                errListeners.fireListeners(
                    "Error receiving packet: " + ex.getMessage() );
            }
            catch( Exception ex )
            {
                ex.printStackTrace();
                break;
            }
        }
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addMessageListener( IUpdater<NetMessage> l )
    {
        msgListeners.addUpdater( l );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addErrorListener( IUpdater<String> l )
    {
        errListeners.addUpdater( l );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addTimeoutListener( IUpdater<SocketTimeoutException> l )
    {
        timeoutListeners.addUpdater( l );
    }

    /***************************************************************************
     * @param buf
     * @return
     * @throws IOException
     **************************************************************************/
    public NetMessage sendMessage( byte [] buf ) throws IOException
    {
        return connection.sendMessage( buf );
    }
}
