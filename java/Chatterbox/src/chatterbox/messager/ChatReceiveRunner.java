package chatterbox.messager;

import java.io.*;
import java.net.*;
import java.util.Arrays;

import org.jutils.concurrent.IStoppable;
import org.jutils.concurrent.IStopper;

import chatterbox.data.ChatHeader;
import chatterbox.data.messages.UserAvailableMessage;
import chatterbox.data.messages.UserLeftMessage;
import chatterbox.io.*;
import chatterbox.model.IChatMessage;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatReceiveRunner implements IStoppable
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
    public ChatReceiveRunner( Chat chat, MulticastSocket socket,
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
    public void run( IStopper stopper )
    {
        byte[] messageBytes;
        ByteArrayInputStream byteStream;

        while( stopper.continueProcessing() )
        {
            try
            {
                socket.receive( rxPacket );
                messageBytes = Arrays.copyOf( rxPacket.getData(),
                    rxPacket.getLength() );
                byteStream = new ByteArrayInputStream( messageBytes );

                try
                {
                    parseMessage( byteStream );
                }
                catch( IOException ex )
                {
                    // TODO log this exception
                    ex.printStackTrace();
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
    private void parseMessage( ByteArrayInputStream stream ) throws IOException
    {
        DataInputStream inStream = new DataInputStream( stream );
        ChatHeader header = headerSerializer.read( inStream );

        switch( header.getMessageType() )
        {
            case Chat:
            {
                IChatMessage message = messageSerializer.read( inStream );
                chat.receiveMessage( message );
                break;
            }
            case UserAvailable:
            {
                UserAvailableMessage message = userAvailSerializer.read( inStream );
                chat.setUserAvailable( message.getUser(), true );
                break;
            }
            case UserLeft:
            {
                UserLeftMessage message = userLeftSerializer.read( inStream );
                chat.removeUser( message.getConversationId(), message.getUser() );
                break;
            }
        }
    }
}
