package org.jutils.net;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.jutils.concurrent.ITask;
import org.jutils.concurrent.ITaskHandler;
import org.jutils.ui.event.updater.IUpdater;
import org.jutils.ui.event.updater.UpdaterList;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ConnectionReceiverTask implements ITask
{
    /**  */
    private final IConnection connection;
    /**  */
    private final UpdaterList<NetMessage> msgListeners;
    /**  */
    private final UpdaterList<String> errListeners;

    /***************************************************************************
     * @param connection
     **************************************************************************/
    public ConnectionReceiverTask( IConnection connection )
    {
        this.connection = connection;
        this.msgListeners = new UpdaterList<>();
        this.errListeners = new UpdaterList<>();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void run( ITaskHandler stopper )
    {
        while( stopper.canContinue() )
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
            }
            catch( SocketException ex )
            {
                // LogUtils.printDebug( "Receive timed out..." );
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
}
