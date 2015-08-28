package chatterbox.messenger;

import java.io.IOException;
import java.util.*;

import org.jutils.io.*;
import org.jutils.ui.event.ItemActionEvent;
import org.jutils.ui.event.ItemActionListener;

import chatterbox.data.*;
import chatterbox.data.messages.UserAvailableMessage;
import chatterbox.data.messages.UserLeftMessage;
import chatterbox.io.*;
import chatterbox.model.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Chat extends AbstractChat
{
    /**  */
    private final IConversation defaultConversation;
    /**  */
    private final Map<String, UserCheckTask> userTasks;
    /**  */
    private final MessageSerializer msgSerializer;

    /**  */
    private Timer userAvailableTimer;
    /**  */
    private final ChatWire wire;

    /**  */
    private ChatConfig config;

    /***************************************************************************
     * @param options
     **************************************************************************/
    public Chat()
    {
        super();

        this.userTasks = new HashMap<String, UserCheckTask>();
        this.defaultConversation = new Conversation( this, "", null );
        this.msgSerializer = new MessageSerializer( getLocalUser() );

        this.userAvailableTimer = null;
        this.wire = new ChatWire( new RawReceiver( this ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void connect( ChatConfig config ) throws IOException
    {
        this.config = config;

        wire.connect( config.address, config.port );

        userAvailableTimer = new Timer( "User Available Timer" );
        userAvailableTimer.schedule( new UserAvailableTask( this ), 0, 5000 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void disconnect()
    {
        for( UserCheckTask task : userTasks.values() )
        {
            task.cancel();
        }

        userAvailableTimer.cancel();
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

    /***************************************************************************
     * @param user
     **************************************************************************/
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
    private void receiveMessage( ChatMessage message )
    {
        // if( !message.isLocalUser() )
        // {
        String conversationId = message.conversation;
        IConversation conversation = getConversation( conversationId );
        if( conversation == null )
        {
            List<IUser> users = Arrays.asList( new IUser[] { message.sender } );
            conversation = new Conversation( this, conversationId, users );
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
            msgSerializer.userAvailableMessageSerializer.write( message, out );

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
            msgSerializer.userLeftMessageSerializer.write( message, out );

            sendBytes( ChatMessageType.UserLeft, stream.toByteArray() );
        }
    }

    /***************************************************************************
     * @param message
     * @throws IOException
     **************************************************************************/
    public void sendMessage( ChatMessage message ) throws IOException
    {
        try( ByteArrayStream stream = new ByteArrayStream( 1024 );
             DataStream out = new DataStream( stream ) )
        {
            // -----------------------------------------------------------------
            // Get the message bytes.
            // -----------------------------------------------------------------
            msgSerializer.messageSerializer.write( message, out );

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

        // Put the header bytes before the message.
        header = new ChatHeader( messageType, msgBytes.length );

        try( ByteArrayStream stream = new ByteArrayStream(
            msgBytes.length + 64 ); DataStream out = new DataStream( stream ) )
        {
            msgSerializer.headerSerializer.write( header, out );
            stream.write( msgBytes );
            msgBytes = stream.toByteArray();

            if( msgBytes.length > 65535 )
            {
                throw new RuntimeFormatException(
                    "Message is too long: " + msgBytes.length );
            }

            wire.send( msgBytes );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public ChatConfig getConfig()
    {
        return config;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class RawReceiver implements ItemActionListener<RawMessage>
    {
        private final Chat chat;
        private final MessageSerializer msgSerializer;

        public RawReceiver( Chat chat )
        {
            this.chat = chat;
            this.msgSerializer = new MessageSerializer( chat.getLocalUser() );
        }

        @Override
        public void actionPerformed( ItemActionEvent<RawMessage> event )
        {
            RawMessage msg = event.getItem();
            byte[] messageBytes = msg.bytes;

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

        private void parseMessage( IDataStream stream )
            throws IOException, RuntimeFormatException
        {
            ChatHeader header = msgSerializer.headerSerializer.read( stream );

            switch( header.getMessageType() )
            {
                case Chat:
                {
                    ChatMessage message = msgSerializer.messageSerializer.read(
                        stream );
                    chat.receiveMessage( message );
                    break;
                }
                case UserAvailable:
                {
                    UserAvailableMessage message = msgSerializer.userAvailableMessageSerializer.read(
                        stream );
                    chat.setUserAvailable( message.getUser(), true );
                    break;
                }
                case UserLeft:
                {
                    UserLeftMessage message = msgSerializer.userLeftMessageSerializer.read(
                        stream );
                    chat.removeUser( message.getConversationId(),
                        message.getUser() );
                    break;
                }
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class MessageSerializer
    {
        public final ChatHeaderSerializer headerSerializer;
        public final ChatMessageSerializer messageSerializer;
        public final UserAvailableMessageSerializer userAvailableMessageSerializer;
        public final UserLeftMessageSerializer userLeftMessageSerializer;

        public MessageSerializer( IUser user )
        {
            this.headerSerializer = new ChatHeaderSerializer();
            this.messageSerializer = new ChatMessageSerializer();
            this.userAvailableMessageSerializer = new UserAvailableMessageSerializer();
            this.userLeftMessageSerializer = new UserLeftMessageSerializer();
        }
    }
}
