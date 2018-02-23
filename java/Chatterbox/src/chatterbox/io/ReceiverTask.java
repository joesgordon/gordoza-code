package chatterbox.io;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

import org.jutils.concurrent.ITask;
import org.jutils.concurrent.ITaskHandler;
import org.jutils.net.NetMessage;
import org.jutils.ui.event.ItemActionEvent;
import org.jutils.ui.event.ItemActionListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ReceiverTask implements ITask
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
    public void run( ITaskHandler stopManager )
    {
        while( stopManager.canContinue() )
        {
            try
            {
                socket.receive( rxPacket );

                byte[] messageBytes = Arrays.copyOf( rxPacket.getData(),
                    rxPacket.getLength() );

                InetAddress address = rxPacket.getAddress();
                int port = rxPacket.getPort();

                rxListener.actionPerformed(
                    new ItemActionEvent<NetMessage>( this, new NetMessage( true,
                        address.getHostAddress(), port, messageBytes ) ) );
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
