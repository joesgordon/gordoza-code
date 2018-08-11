package org.jutils.net;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.jutils.concurrent.ITask;
import org.jutils.concurrent.ITaskHandler;
import org.jutils.ui.event.ItemActionList;
import org.jutils.ui.event.ItemActionListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ConnectionReceiverTask implements ITask
{
    /**  */
    private final IConnection connection;
    /**  */
    private final ItemActionList<NetMessage> msgListeners;
    /**  */
    private final ItemActionList<String> errListeners;

    /***************************************************************************
     * @param connection
     **************************************************************************/
    public ConnectionReceiverTask( IConnection connection )
    {
        this.connection = connection;
        this.msgListeners = new ItemActionList<>();
        this.errListeners = new ItemActionList<>();
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
                msgListeners.fireListeners( this, msg );
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
                errListeners.fireListeners( this,
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
    public void addMessageListener( ItemActionListener<NetMessage> l )
    {
        msgListeners.addListener( l );
    }

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addErrorListener( ItemActionListener<String> l )
    {
        errListeners.addListener( l );
    }
}
