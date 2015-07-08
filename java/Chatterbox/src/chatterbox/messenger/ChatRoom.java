package chatterbox.messenger;

import java.io.IOException;
import java.net.*;
import java.util.*;

import org.jutils.concurrent.Stoppable;
import org.jutils.io.*;

import chatterbox.data.ChatHeader;
import chatterbox.data.ChatMessageType;
import chatterbox.data.messages.UserAvailableMessage;
import chatterbox.data.messages.UserLeftMessage;
import chatterbox.io.*;
import chatterbox.model.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatRoom extends AbstractChatRoom
{
    /**  */
    private MulticastSocket socket;
    /**  */
    private IConversation defaultConversation;
    /**  */
    private ChatHeaderSerializer headerSerializer;
    /**  */
    private ChatMessageSerializer chatMessageSerializer;
    /**  */
    private UserAvailableMessageSerializer userAvailableMessageSerializer;
    /**  */
    private UserLeftMessageSerializer userLeftMessageSerializer;
    /**  */
    private InetAddress address;
    /**  */
    private int port;
    /**  */
    private byte[] rxBuffer;
    /**  */
    private DatagramPacket rxPacket;
    /**  */
    private Stoppable receiveThread;
    /**  */
    private ChatReceiveTask receiver;
    /**  */
    private Map<String, UserCheckTask> userTasks;
    /**  */
    private Timer userAvailableTimer;

    /***************************************************************************
     * @param options
     **************************************************************************/
    public ChatRoom()
    {
        super();

        userTasks = new HashMap<String, UserCheckTask>();
        headerSerializer = new ChatHeaderSerializer();
        chatMessageSerializer = new ChatMessageSerializer( getLocalUser() );
        userAvailableMessageSerializer = new UserAvailableMessageSerializer();
        userLeftMessageSerializer = new UserLeftMessageSerializer();
        rxBuffer = new byte[Short.MAX_VALUE];
        rxPacket = new DatagramPacket( rxBuffer, rxBuffer.length, address, port );

        defaultConversation = new Conversation( this, "", null );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void connect( String group, int port ) throws IOException
    {
        address = InetAddress.getByName( group );
        this.port = port;
        socket = new MulticastSocket( port );

        socket.setTimeToLive( 10 );
        socket.joinGroup( address );
        socket.setSoTimeout( 1000 );

        receiver = new ChatReceiveTask( this, socket, rxPacket );
        receiveThread = new Stoppable( receiver );
        Thread thread = new Thread( receiveThread );
        thread.setName( "Message Receive Thread" );
        thread.start();

        userAvailableTimer = new Timer( "User Available Timer" );
        userAvailableTimer.schedule( new UserAvailableTask( this ), 0, 5000 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void disconnect()
    {
        try
        {
            sendMessage( new UserLeftMessage( "", getLocalUser() ) );
            socket.leaveGroup( address );
        }
        catch( IOException ex )
        {
            throw new RuntimeException( ex );
        }

        for( UserCheckTask task : userTasks.values() )
        {
            task.cancel();
        }

        userAvailableTimer.cancel();
        try
        {
            receiveThread.stopAndWaitFor();
        }
        catch( InterruptedException ex )
        {
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IConversation createConversation( List<IUser> users )
    {
        IConversation conversation = new Conversation( this,
            getNextConversationId(), users );
        this.addConversation( conversation );
        return conversation;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IConversation getDefaultConversation()
    {
        return defaultConversation;
    }

    /***************************************************************************
     * @param message
     **************************************************************************/
    public void setUserAvailable( IUser user, boolean available )
    {
        if( user.equals( getLocalUser() ) )
        {
            return;
        }

        List<IConversation> conversations = getConversations();

        for( IConversation conv : conversations )
        {
            conv.setUserAvailable( user, available );
        }

        if( !defaultConversation.getUsers().contains( user ) )
        {
            defaultConversation.addUser( user );
        }
        defaultConversation.setUserAvailable( user, available );

        UserCheckTask task = userTasks.get( user.getUserId() );
        if( task == null )
        {
            task = new UserCheckTask( this, user );
            userTasks.put( user.getUserId(), task );
        }
        else if( available )
        {
            task.reset();
        }
        else
        {
            task.advance();
        }
    }

    public void removeUser( IUser user )
    {
        List<IConversation> conversations = getConversations();

        for( IConversation conv : conversations )
        {
            conv.removeUser( user );
        }
    }

    /***************************************************************************
     * @param message
     **************************************************************************/
    public void removeUser( String conversationId, IUser user )
    {
        IConversation conversation = getConversation( conversationId );
        if( conversation != null )
        {
            conversation.removeUser( user );
        }
    }

    /***************************************************************************
     * @param message
     **************************************************************************/
    public void receiveMessage( IChatMessage message )
    {
        // if( !message.isLocalUser() )
        // {
        String conversationId = message.getConversationId();
        IConversation conversation = getConversation( conversationId );
        if( conversation == null )
        {
            List<IUser> users = Arrays.asList( new IUser[] { message.getSender() } );
            conversation = new Conversation( this, message.getConversationId(),
                users );
        }
        conversation.receiveMessage( message );
        // }
    }

    /***************************************************************************
     * @param message
     * @throws IOException
     **************************************************************************/
    public void sendMessage( UserAvailableMessage message ) throws IOException
    {
        try( ByteArrayStream stream = new ByteArrayStream( 1 );
             DataStream out = new DataStream( stream ) )
        {
            // -----------------------------------------------------------------
            // Get the message bytes.
            // -----------------------------------------------------------------
            userAvailableMessageSerializer.write( message, out );

            byte[] array = stream.toByteArray();

            sendBytes( ChatMessageType.UserAvailable, array );
        }
    }

    /***************************************************************************
     * @param message
     * @throws IOException
     **************************************************************************/
    public void sendMessage( UserLeftMessage message ) throws IOException
    {
        try( ByteArrayStream stream = new ByteArrayStream( 1024 );
             DataStream out = new DataStream( stream ) )
        {
            // -----------------------------------------------------------------
            // Get the message bytes.
            // -----------------------------------------------------------------
            userLeftMessageSerializer.write( message, out );

            sendBytes( ChatMessageType.UserLeft, stream.toByteArray() );
        }
    }

    /***************************************************************************
     * @param message
     * @throws IOException
     **************************************************************************/
    public void sendMessage( IChatMessage message ) throws IOException
    {
        try( ByteArrayStream stream = new ByteArrayStream( 1024 );
             DataStream out = new DataStream( stream ) )
        {
            // -----------------------------------------------------------------
            // Get the message bytes.
            // -----------------------------------------------------------------
            chatMessageSerializer.write( message, out );

            sendBytes( ChatMessageType.Chat, stream.toByteArray() );
        }
    }

    /***************************************************************************
     * @param msgBytes
     * @throws IOException
     **************************************************************************/
    private void sendBytes( ChatMessageType messageType, byte[] msgBytes )
        throws IOException, RuntimeFormatException
    {
        ChatHeader header;
        DatagramPacket packet;

        // Put the header bytes before the message.
        header = new ChatHeader( messageType, msgBytes.length );

        try( ByteArrayStream stream = new ByteArrayStream( msgBytes.length + 64 );
             DataStream out = new DataStream( stream ) )
        {
            headerSerializer.write( header, out );
            stream.write( msgBytes );
            msgBytes = stream.toByteArray();

            if( msgBytes.length > 65535 )
            {
                throw new RuntimeFormatException( "Message is too long: " +
                    msgBytes.length );
            }

            packet = new DatagramPacket( msgBytes, msgBytes.length, address,
                port );
            socket.send( packet );
        }
    }

    @Override
    public String getAddress()
    {
        return address.getHostAddress();
    }

    @Override
    public int getPort()
    {
        return port;
    }
}
