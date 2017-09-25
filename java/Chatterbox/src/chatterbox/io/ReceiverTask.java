package chatterbox.io;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

import org.jutils.concurrent.IStoppableTask;
import org.jutils.concurrent.ITaskStopManager;
import org.jutils.net.NetMessage;
import org.jutils.ui.event.ItemActionEvent;
import org.jutils.ui.event.ItemActionListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ReceiverTask implements IStoppableTask
{
    /**  */
    private final ItemActionListener<NetMessage> rxListener;
    /**  */
    private final MulticastSocket socket;
    /**  */
    private final DatagramPacket rxPacket;

    /***************************************************************************
     * @param rxListener
     * @param socket
     **************************************************************************/
    public ReceiverTask( ItemActionListener<NetMessage> rxListener,
        MulticastSocket socket )
    {
        this.rxListener = rxListener;
        this.socket = socket;

        byte[] rxBuffer = new byte[Short.MAX_VALUE];
        this.rxPacket = new DatagramPacket( rxBuffer, rxBuffer.length,
            socket.getInetAddress(), socket.getLocalPort() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void run( ITaskStopManager stopManager )
    {
        while( stopManager.continueProcessing() )
        {
            try
            {
                socket.receive( rxPacket );

                byte[] messageBytes = Arrays.copyOf( rxPacket.getData(),
                    rxPacket.getLength() );

                InetAddress address = rxPacket.getAddress();
                int port = rxPacket.getPort();

                rxListener.actionPerformed( new ItemActionEvent<NetMessage>(
                    this, new NetMessage( messageBytes, address, port ) ) );
            }
            catch( SocketTimeoutException ex )
            {
                ;
            }
            catch( IOException ex )
            {
                throw new RuntimeException( ex );
            }
            catch( Exception ex )
            {
                throw new RuntimeException( ex );
            }
        }
    }
}
