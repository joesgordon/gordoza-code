package chatterbox.messenger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;

import org.jutils.ValidationException;
import org.jutils.io.ByteArrayStream;
import org.jutils.io.DataStream;
import org.jutils.io.IDataStream;
import org.jutils.io.LogUtils;
import org.jutils.io.options.OptionsSerializer;
import org.jutils.net.ConnectionListener;
import org.jutils.net.IConnection;
import org.jutils.net.MulticastConnection;
import org.jutils.net.MulticastInputs;
import org.jutils.net.NetMessage;
import org.jutils.task.TaskError;
import org.jutils.task.TaskRunner;
import org.jutils.ui.event.ItemActionList;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.event.updater.IUpdater;

import chatterbox.ChatterboxConstants;
import chatterbox.data.ChatInfo;
import chatterbox.data.ChatUser;
import chatterbox.data.ChatterboxOptions;
import chatterbox.data.messages.ChatHeader;
import chatterbox.data.messages.ChatMessage;
import chatterbox.data.messages.ChatMessageType;
import chatterbox.data.messages.UserAvailableMessage;
import chatterbox.data.messages.UserLeftMessage;
import chatterbox.io.ChatHeaderSerializer;
import chatterbox.io.ChatMessageSerializer;
import chatterbox.io.UserAvailableMessageSerializer;
import chatterbox.io.UserLeftMessageSerializer;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatterboxHandler
{
    /**  */
    private final ChatUser localUser;
    /**  */
    private final Map<String, ChatHandler> conversations;
    /**  */
    private final ItemActionList<ChatHandler> conversationCreatedListeners;
    /**  */
    private final ChatHandler defaultConversation;
    /**  */
    private final MessageSerializer msgSerializer;

    /**  */
    private ConnectionListener wire;
    /**  */
    private UserCheckTask userTask;
    /**  */
    private TaskRunner userRunner;
    /**  */
    private Thread userThread;

    /***************************************************************************
     * @param user
     **************************************************************************/
    public ChatterboxHandler( ChatUser user )
    {
        this.localUser = user;

        this.conversations = new HashMap<String, ChatHandler>( 100 );
        this.conversationCreatedListeners = new ItemActionList<ChatHandler>();

        this.defaultConversation = new ChatHandler( this,
            new ChatInfo( "", "Default Conversation" ) );
        this.msgSerializer = new MessageSerializer();
        this.userTask = null;

        conversations.put( defaultConversation.info.id, defaultConversation );
    }

    /***************************************************************************
     * @param config
     **************************************************************************/
    public void connect( MulticastInputs config ) throws IOException
    {
        try
        {
            @SuppressWarnings( "resource")
            IConnection connection = new MulticastConnection( config );
            IUpdater<String> errorListener = (
                e ) -> SwingUtilities.invokeLater(
                    () -> displayErrorMessage( e ) );
            this.wire = new ConnectionListener();
            wire.start( connection );
            wire.addMessageListener( new RawReceiver( this ) );
            wire.addErrorListener( errorListener );
        }
        catch( IOException ex )
        {
            // ex.printStackTrace();

            throw ex;
        }

        this.userTask = new UserCheckTask( this );
        this.userRunner = new TaskRunner( userTask,
            new SignalerTaskHander( new Signaler() ) );
        this.userThread = new Thread( userRunner, "User Checking Thread" );
        userThread.start();

        // getLocalUser().displayName = config.displayName;
    }

    /***************************************************************************
     * @param errorMessage
     **************************************************************************/
    private static void displayErrorMessage( String errorMessage )
    {
        LogUtils.printError( errorMessage );
        // TODO Auto-generated method stub and run on swing event queue
    }

    /***************************************************************************
     * 
     **************************************************************************/
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

            try
            {
                wire.close();
            }
            catch( IOException e )
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            for( ChatUser u : new ArrayList<>(
                defaultConversation.getUsers() ) )
            {
                defaultConversation.removeUser( u );
            }

            for( ChatHandler c : new ArrayList<>( getConversations() ) )
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
     * @param users
     * @return
     **************************************************************************/
    public ChatHandler createConversation( List<String> users )
    {
        String id = getNextConversationId();
        ChatInfo info = new ChatInfo( id, "Chatty Chatty Bang Bang" );
        info.users.addAll( users );
        ChatHandler conversation = new ChatHandler( this, info );
        this.addConversation( conversation );
        return conversation;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public ChatHandler getDefaultConversation()
    {
        return defaultConversation;
    }

    /***************************************************************************
     * @param message
     **************************************************************************/
    public void setUserAvailable( ChatUser user )
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

                OptionsSerializer<ChatterboxOptions> options;

                options = ChatterboxConstants.getOptions();
                options.getOptions().displayName = user.displayName;
                options.write();
            }

            // return;
        }

        List<ChatHandler> conversations = getConversations();

        for( ChatHandler conv : conversations )
        {
            conv.setUserAvailable( user );
        }

        if( !defaultConversation.getUsers().contains( user ) )
        {
            defaultConversation.addUser( user );
        }

        defaultConversation.setUserAvailable( user );

        userTask.markSeen( user );
    }

    /***************************************************************************
     * @param user
     **************************************************************************/
    public void removeUser( ChatUser user )
    {
        List<ChatHandler> conversations = getConversations();

        for( ChatHandler conv : conversations )
        {
            conv.removeUser( user );
        }
    }

    /***************************************************************************
     * @param conversationId
     * @param user
     **************************************************************************/
    public void removeUser( String conversationId, ChatUser user )
    {
        ChatHandler conversation = getConversation( conversationId );
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
        String conversationId = message.chatId;
        ChatHandler conversation = getConversation( conversationId );

        if( conversation != null )
        {
            conversation.receiveMessage( message );
        }
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

            wire.sendMessage( msgBytes );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class RawReceiver implements IUpdater<NetMessage>
    {
        /**  */
        private final ChatterboxHandler chat;
        /**  */
        private final MessageSerializer msgSerializer;

        /**
         * @param chat
         */
        public RawReceiver( ChatterboxHandler chat )
        {
            this.chat = chat;
            this.msgSerializer = new MessageSerializer();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void update( NetMessage msg )
        {
            try( ByteArrayStream byteStream = new ByteArrayStream(
                msg.contents );
                 IDataStream stream = new DataStream( byteStream ); )
            {
                parseMessage( stream );
            }
            catch( IOException ex )
            {
                ChatterboxHandler.displayErrorMessage(
                    "I/O error: " + ex.getMessage() );
            }
            catch( ValidationException ex )
            {
                ChatterboxHandler.displayErrorMessage( ex.getMessage() );
            }
        }

        /**
         * @param stream
         * @throws IOException
         * @throws ValidationException
         */
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
                    chat.setUserAvailable( message.user );
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
        /**  */
        public final ChatHeaderSerializer headerSerializer;
        /**  */
        public final ChatMessageSerializer messageSerializer;
        /**  */
        public final UserAvailableMessageSerializer userAvailableMessageSerializer;
        /**  */
        public final UserLeftMessageSerializer userLeftMessageSerializer;

        /**
         * 
         */
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
        /**
         * {@inheritDoc}
         */
        @Override
        public void signalMessage( String message )
        {
            // TODO Auto-generated method stub
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean signalPercent( int percent )
        {
            // TODO Auto-generated method stub
            return true;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void signalError( TaskError error )
        {
            // TODO Auto-generated method stub
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void signalFinished()
        {
            // TODO Auto-generated method stub
        }
    }

    /**
     * @param userId
     * @return
     */
    public ChatUser getUser( String userId )
    {
        for( ChatUser user : defaultConversation.getUsers() )
        {
            if( user.userId.equals( userId ) )
            {
                return user;
            }
        }

        return new ChatUser( userId );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public final void addConversationCreatedListener(
        ItemActionListener<ChatHandler> l )
    {
        conversationCreatedListeners.addListener( l );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    protected final String getNextConversationId()
    {
        return localUser.userId + "@" + new Date().getTime();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public final ChatUser getLocalUser()
    {
        return localUser;
    }

    /***************************************************************************
     * @param conversation
     **************************************************************************/
    protected final void addConversation( ChatHandler conversation )
    {
        conversations.put( conversation.getConversationId(), conversation );
    }

    /***************************************************************************
     * @param conversationId
     * @return
     **************************************************************************/
    protected final ChatHandler getConversation( String conversationId )
    {
        if( conversationId.equals(
            getDefaultConversation().getConversationId() ) )
        {
            return getDefaultConversation();
        }
        return conversations.get( conversationId );
    }

    /***************************************************************************
     * @param conversation
     **************************************************************************/
    public final void removeConversation( ChatHandler conversation )
    {
        if( conversation == getDefaultConversation() )
        {
            disconnect();
        }
        else
        {
            conversations.remove( conversation.getConversationId() );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public List<ChatHandler> getConversations()
    {
        return new ArrayList<ChatHandler>( conversations.values() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public List<ChatInfo> getConversationInfos()
    {
        List<ChatInfo> infos = new ArrayList<>( conversations.size() );

        for( ChatHandler chat : conversations.values() )
        {
            infos.add( chat.info );
        }

        return infos;
    }
}
