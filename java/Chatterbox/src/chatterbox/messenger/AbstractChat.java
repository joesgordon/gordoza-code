package chatterbox.messenger;

import java.lang.management.ManagementFactory;
import java.util.*;

import org.jutils.ui.event.ItemActionList;
import org.jutils.ui.event.ItemActionListener;

import chatterbox.data.ChatUser;
import chatterbox.model.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public abstract class AbstractChat implements IChat
{
    /**  */
    private final IUser localUser;
    /**  */
    private final Map<String, IConversation> conversations;
    /**  */
    private final ItemActionList<IConversation> conversationCreatedListeners;
    /**  */
    private final String jvmName;

    /***************************************************************************
     * 
     **************************************************************************/
    public AbstractChat()
    {
        conversations = new HashMap<String, IConversation>( 100 );
        conversationCreatedListeners = new ItemActionList<IConversation>();
        localUser = new ChatUser( System.getProperty( "user.name" ) );
        jvmName = ManagementFactory.getRuntimeMXBean().getName();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
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
    @Override
    public final IUser getLocalUser()
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
        if( conversationId.equals( getDefaultConversation().getConversationId() ) )
        {
            return getDefaultConversation();
        }
        return conversations.get( conversationId );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
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
}
