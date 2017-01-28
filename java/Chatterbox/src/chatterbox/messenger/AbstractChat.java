package chatterbox.messenger;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.*;

import org.jutils.ui.event.ItemActionList;
import org.jutils.ui.event.ItemActionListener;

import chatterbox.data.ChatConfig;
import chatterbox.data.ChatUser;
import chatterbox.model.IConversation;

/*******************************************************************************
 * 
 ******************************************************************************/
public abstract class AbstractChat
{
    /**  */
    private final ChatUser localUser;
    /**  */
    private final Map<String, IConversation> conversations;
    /**  */
    private final ItemActionList<IConversation> conversationCreatedListeners;
    /**  */
    private final String jvmName;

    /**
     * @param user *
     * ************************************************************************
     **************************************************************************/
    public AbstractChat( ChatUser user )
    {
        this.conversations = new HashMap<String, IConversation>( 100 );
        this.conversationCreatedListeners = new ItemActionList<IConversation>();
        this.localUser = user;
        this.jvmName = ManagementFactory.getRuntimeMXBean().getName();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public final void addConversationCreatedListener(
        ItemActionListener<IConversation> l )
    {
        conversationCreatedListeners.addListener( l );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    protected final String getNextConversationId()
    {
        return jvmName + "@" + new Date().getTime();
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
    protected final void addConversation( IConversation conversation )
    {
        conversations.put( conversation.getConversationId(), conversation );
    }

    /***************************************************************************
     * @param conversationId
     * @return
     **************************************************************************/
    protected final IConversation getConversation( String conversationId )
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
    public final void removeConversation( IConversation conversation )
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
    protected List<IConversation> getConversations()
    {
        return new ArrayList<IConversation>( conversations.values() );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public abstract IConversation getDefaultConversation();

    /***************************************************************************
     * 
     **************************************************************************/
    public abstract void disconnect();

    /***************************************************************************
     * @param config
     * @throws IOException
     **************************************************************************/
    public abstract void connect( ChatConfig config ) throws IOException;

    /***************************************************************************
     * @param users
     * @return
     **************************************************************************/
    public abstract IConversation createConversation( List<ChatUser> users );

    /***************************************************************************
     * @return
     **************************************************************************/
    public abstract ChatConfig getConfig();
}
