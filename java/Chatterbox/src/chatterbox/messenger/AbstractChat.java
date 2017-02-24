package chatterbox.messenger;

import java.io.IOException;
import java.util.*;

import org.jutils.ui.event.ItemActionList;
import org.jutils.ui.event.ItemActionListener;
import org.mc.io.MulticastInputs;

import chatterbox.data.ChatUser;

/*******************************************************************************
 * 
 ******************************************************************************/
public abstract class AbstractChat
{
    /**  */
    private final ChatUser localUser;
    /**  */
    private final Map<String, Conversation> conversations;
    /**  */
    private final ItemActionList<Conversation> conversationCreatedListeners;

    /***************************************************************************
     * @param user
     **************************************************************************/
    public AbstractChat( ChatUser user )
    {
        this.localUser = user;

        this.conversations = new HashMap<String, Conversation>( 100 );
        this.conversationCreatedListeners = new ItemActionList<Conversation>();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public final void addConversationCreatedListener(
        ItemActionListener<Conversation> l )
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
     * 
     **************************************************************************/
    public final ChatUser getLocalUser()
    {
        return localUser;
    }

    /***************************************************************************
     * @param conversation
     **************************************************************************/
    protected final void addConversation( Conversation conversation )
    {
        conversations.put( conversation.getConversationId(), conversation );
    }

    /***************************************************************************
     * @param conversationId
     * @return
     **************************************************************************/
    protected final Conversation getConversation( String conversationId )
    {
        if( conversationId.equals(
            getDefaultConversation().getConversationId() ) )
        {
            return getDefaultConversation();
        }
        return conversations.get( conversationId );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public final void removeConversation( Conversation conversation )
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
    protected List<Conversation> getConversations()
    {
        return new ArrayList<Conversation>( conversations.values() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public abstract Conversation getDefaultConversation();

    /***************************************************************************
     * 
     **************************************************************************/
    public abstract void disconnect();

    /***************************************************************************
     * @param config
     * @throws IOException
     **************************************************************************/
    public abstract void connect( MulticastInputs config ) throws IOException;

    /***************************************************************************
     * @param users
     * @return
     **************************************************************************/
    public abstract Conversation createConversation( List<ChatUser> users );
}
