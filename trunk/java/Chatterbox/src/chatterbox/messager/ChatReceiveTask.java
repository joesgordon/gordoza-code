package chatterbox.messager;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

import org.jutils.concurrent.IStoppableTask;
import org.jutils.concurrent.ITaskStopManager;
import org.jutils.io.*;

import chatterbox.data.ChatHeader;
import chatterbox.data.messages.UserAvailableMessage;
import chatterbox.data.messages.UserLeftMessage;
import chatterbox.io.*;
import chatterbox.model.IChatMessage;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatReceiveTask implements IStoppableTask
{
    /**  */
    private Chat chat;
    /**  */
    private ChatHeaderSerializer headerSerializer;
    /**  */
    private ChatMessageSerializer messageSerializer;
    /**  */
    private UserAvailableMessageSerializer userAvailSerializer;
    /**  */
    private UserLeftMessageSerializer userLeftSerializer;
    /**  */
    private MulticastSocket socket;
    /**  */
    private DatagramPacket rxPacket;

    /***************************************************************************
     * @param chat
     * @param socket
     * @param rxPacket
     **************************************************************************/
    public ChatReceiveTask( Chat chat, MulticastSocket socket,
        DatagramPacket rxPacket )
    {
        this.chat = chat;
        this.socket = socket;
        this.rxPacket = rxPacket;
        messageSerializer = new ChatMessageSerializer( chat.getLocalUser() );
        headerSerializer = new ChatHeaderSerializer();
        userAvailSerializer = new UserAvailableMessageSerializer();
        userLeftSerializer = new UserLeftMessageSerializer();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void run( ITaskStopManager stopper )
    {
        byte[] messageBytes;
        ;

        while( stopper.continueProcessing() )
        {
            try
            {
                socket.receive( rxPacket );
                messageBytes = Arrays.copyOf( rxPacket.getData(),
                    rxPacket.getLength() );

                try( ByteArrayStream byteStream = new ByteArrayStream(
                         messageBytes );
                     IDataStream stream = new DataStream( byteStream ); )
                {
                    parseMessage( stream );
                }
                catch( IOException ex )
                {
                    LogUtils.printError( "I/O error: " + ex.getMessage() );
                }
                catch( RuntimeFormatException ex )
                {
                    LogUtils.printWarning( ex.getMessage() );
                }
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

    /***************************************************************************
     * @param stream
     * @throws IOException
     **************************************************************************/
    private void parseMessage( IDataStream stream ) throws IOException,
        RuntimeFormatException
    {
        ChatHeader header = headerSerializer.read( stream );

        switch( header.getMessageType() )
        {
            case Chat:
            {
                IChatMessage message = messageSerializer.read( stream );
                chat.receiveMessage( message );
                break;
            }
            case UserAvailable:
            {
                UserAvailableMessage message = userAvailSerializer.read( stream );
                chat.setUserAvailable( message.getUser(), true );
                break;
            }
            case UserLeft:
            {
                UserLeftMessage message = userLeftSerializer.read( stream );
                chat.removeUser( message.getConversationId(), message.getUser() );
                break;
            }
        }
    }
}
