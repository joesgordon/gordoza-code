package chatterbox.io;

import java.io.IOException;
import java.net.*;

import org.jutils.ValidationException;
import org.jutils.concurrent.Stoppable;
import org.jutils.ui.event.ItemActionListener;

import chatterbox.data.messages.RawMessage;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatWire
{
    /**  */
    private MulticastSocket socket;
    /**  */
    private InetAddress address;
    /**  */
    private Stoppable rxRunner;
    /**  */
    private Thread rxThread;
    /**  */
    private ReceiverTask receiver;
    /**  */
    private final ItemActionListener<RawMessage> rxListener;

    /***************************************************************************
     * @param rxListener
     **************************************************************************/
    public ChatWire( ItemActionListener<RawMessage> rxListener )
    {
        this.rxListener = rxListener;
    }

    /***************************************************************************
     * @param address
     * @param port
     * @throws IOException
     **************************************************************************/
    public void connect( String address, int port ) throws IOException
    {
        this.address = InetAddress.getByName( address );
        this.socket = new MulticastSocket( port );

        socket.setTimeToLive( 10 );
        socket.joinGroup( this.address );
        socket.setSoTimeout( 1000 );

        this.receiver = new ReceiverTask( rxListener, socket );
        this.rxRunner = new Stoppable( receiver );
        this.rxThread = new Thread( rxRunner );

        rxThread.setName( "Message Receive Thread" );
        rxThread.start();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void disconnect()
    {
        if( socket != null )
        {
            try
            {
                socket.leaveGroup( address );
            }
            catch( IOException ex )
            {
                throw new RuntimeException( ex );
            }
            finally
            {
                socket = null;
            }

            try
            {
                rxRunner.stop();
                rxThread.interrupt();
                rxRunner.stopAndWaitFor();
            }
            catch( InterruptedException ex )
            {
            }
            finally
            {
                rxRunner = null;
            }
        }
    }

    /***************************************************************************
     * @param bytes
     * @throws IOException
     * @throws ValidationException
     **************************************************************************/
    public void send( byte[] msgBytes ) throws IOException, ValidationException
    {
        if( msgBytes.length > 65535 )
        {
            throw new ValidationException(
                "Message is too long: " + msgBytes.length );
        }

        DatagramPacket packet = new DatagramPacket( msgBytes, msgBytes.length,
            address, getPort() );
        socket.send( packet );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String getAddress()
    {
        return socket.getInetAddress().getHostAddress();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getPort()
    {
        return socket == null ? -2 : socket.getLocalPort();
    }
}
