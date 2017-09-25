package org.jutils.net;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.jutils.concurrent.IStoppableTask;
import org.jutils.concurrent.ITaskStopManager;
import org.jutils.ui.event.ItemActionList;
import org.jutils.ui.event.ItemActionListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ConnectionReceiver implements IStoppableTask
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
    public ConnectionReceiver( IConnection connection )
    {
        this.connection = connection;
        this.msgListeners = new ItemActionList<>();
        this.errListeners = new ItemActionList<>();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void run( ITaskStopManager stopper )
    {
        while( stopper.continueProcessing() )
        {
            try
            {
                // LogUtils.printDebug( "Receiving message..." );
                NetMessage msg = connection.rxMessage();
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
            catch( IOException ex )
            {
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
