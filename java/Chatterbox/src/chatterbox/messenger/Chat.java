package chatterbox.messenger;

import java.io.IOException;
import java.util.*;

import org.jutils.ValidationException;
import org.jutils.io.*;
import org.jutils.io.options.OptionsSerializer;
import org.jutils.task.TaskError;
import org.jutils.task.TaskRunner;
import org.jutils.ui.event.ItemActionEvent;
import org.jutils.ui.event.ItemActionListener;

import chatterbox.ChatterboxConstants;
import chatterbox.data.*;
import chatterbox.data.messages.UserAvailableMessage;
import chatterbox.data.messages.UserLeftMessage;
import chatterbox.io.*;
import chatterbox.model.ChatMessage;
import chatterbox.model.IConversation;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Chat extends AbstractChat
{
    /**  */
    private final IConversation defaultConversation;
    /**  */
    private final MessageSerializer msgSerializer;
    /**  */
    private final ChatWire wire;

    /**  */
    private UserCheckTask userTask;
    /**  */
    private TaskRunner userRunner;
    /**  */
    private Thread userThread;
    /**  */
    private ChatConfig config;

    /***************************************************************************
     * @param user
     * @param options
     **************************************************************************/
    public Chat( ChatUser user )
    {
        super( user );

        this.defaultConversation = new Conversation( this, "", null );
        this.msgSerializer = new MessageSerializer();
        this.wire = new ChatWire( new RawReceiver( this ) );
        this.userTask = null;
        this.config = null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void connect( ChatConfig config ) throws IOException
    {
        this.config = config;
        this.userTask = new UserCheckTask( this );
        this.userRunner = new TaskRunner( userTask,
            new SignalerTaskHander( new Signaler() ) );

        wire.connect( config.address, config.port );

        this.userThread = new Thread( userRunner, "User Checking Thread" );
        userThread.start();

        // getLocalUser().displayName = config.displayName;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void disconnect()
    {
        if( userRunner != null )
        {
            try
            {
                userRunner.stop();
                userThread.interrupt();
                userRunner.stopAndWait();
            }
            catch( InterruptedException e )
            {
            }

            wire.disconnect();

            for( ChatUser u : new ArrayList<>(
                defaultConversation.getUsers() ) )
            {
                defaultConversation.removeUser( u );
            }

            for( IConversation c : new ArrayList<>( getConversations() ) )
            {
                if( c != getDefaultConversation() )
                {
                    removeConversation( c );
                }
            }

            userTask = null;
            userRunner = null;
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IConversation createConversation( List<ChatUser> users )
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
    public void setUserAvailable( ChatUser user, boolean available )
    {
        ChatUser localMe = getLocalUser();

        if( user.equals( localMe ) )
        {
            // LogUtils.printDebug( "Got me " + String.format( "%s: %s vs %s:
            // %s",
            // user.getUserId(), user.getDisplayName(), localMe.getUserId(),
            // localMe.getDisplayName() ) );

            if( !user.displayName.equals( localMe.displayName ) )
            {
                localMe.displayName = user.displayName;

                OptionsSerializer<ChatterConfig> options;

                options = ChatterboxConstants.getOptions();
                options.getOptions().chatCfg.displayName = user.displayName;
                options.write();
            }

            // return;
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

        userTask.markSeen( user );
    }

    /***************************************************************************
     * @param user
     **************************************************************************/
    public void removeUser( ChatUser user )
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
    public void removeUser( String conversationId, ChatUser user )
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
            List<ChatUser> users = Arrays.asList(
                new ChatUser[] { message.sender } );
            conversation = new Conversation( this, conversationId, users );
        }
        conversation.receiveMessage( message );
        // }
    }

    /***************************************************************************
     * @param message
     * @throws IOException
     * @throws ValidationException
     **************************************************************************/
    public void sendMessage( UserAvailableMessage message )
        throws IOException, ValidationException
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
     * @throws ValidationException
     **************************************************************************/
    public void sendMessage( UserLeftMessage message )
        throws IOException, ValidationException
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
     * @throws ValidationException
     **************************************************************************/
    public void sendMessage( ChatMessage message )
        throws IOException, ValidationException
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
        throws IOException, ValidationException
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
                throw new ValidationException(
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
            this.msgSerializer = new MessageSerializer();
        }

        @Override
        public void actionPerformed( ItemActionEvent<RawMessage> event )
        {
            RawMessage msg = event.getItem();

            try( ByteArrayStream byteStream = new ByteArrayStream( msg.bytes );
                 IDataStream stream = new DataStream( byteStream ); )
            {
                parseMessage( stream );
            }
            catch( IOException ex )
            {
                LogUtils.printError( "I/O error: " + ex.getMessage() );
            }
            catch( ValidationException ex )
            {
                LogUtils.printWarning( ex.getMessage() );
            }
        }

        private void parseMessage( IDataStream stream )
            throws IOException, ValidationException
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
                    chat.setUserAvailable( message.user, true );
                    break;
                }
                case UserLeft:
                {
                    UserLeftMessage message = msgSerializer.userLeftMessageSerializer.read(
                        stream );
                    chat.removeUser( message.conversationId, message.user );
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

        public MessageSerializer()
        {
            this.headerSerializer = new ChatHeaderSerializer();
            this.messageSerializer = new ChatMessageSerializer();
            this.userAvailableMessageSerializer = new UserAvailableMessageSerializer();
            this.userLeftMessageSerializer = new UserLeftMessageSerializer();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class Signaler implements ISignaler
    {
        @Override
        public void signalMessage( String message )
        {
            // TODO Auto-generated method stub
        }

        @Override
        public boolean signalPercent( int percent )
        {
            // TODO Auto-generated method stub
            return true;
        }

        @Override
        public void signalError( TaskError error )
        {
            // TODO Auto-generated method stub
        }

        @Override
        public void signalFinished()
        {
            // TODO Auto-generated method stub
        }
    }
}
