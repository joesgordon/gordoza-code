package chatterbox.messenger;

import java.util.ArrayList;
import java.util.List;

import org.jutils.ui.event.ItemActionList;
import org.jutils.ui.event.ItemActionListener;

import chatterbox.data.ChatUser;
import chatterbox.model.*;

/***************************************************************************
 * 
 **************************************************************************/
public abstract class AbstractConversation implements IConversation
{
    /**  */
    private String localId;
    /**  */
    private Chat chat;
    /**  */
    private List<ChatUser> users;

    /**  */
    protected ItemActionList<ChatMessage> messageReceivedListeners;

    /**  */
    protected List<IUserListener> userListeners;

    /***************************************************************************
     * @param id
     * @param remoteId
     * @param chat
     * @param user
     **************************************************************************/
    public AbstractConversation( Chat chat, String id, List<ChatUser> users )
    {
        this.localId = id;
        this.chat = chat;
        this.users = new ArrayList<ChatUser>();

        if( users != null )
        {
            this.users.addAll( users );
        }

        userListeners = new ArrayList<>();
        messageReceivedListeners = new ItemActionList<ChatMessage>();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public final void addMessageReceivedListener(
        ItemActionListener<ChatMessage> l )
    {
        messageReceivedListeners.addListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addUserListener( IUserListener l )

    {
        userListeners.add( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public final Chat getChat()
    {
        return chat;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public final String getConversationId()
    {
        return localId;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public final List<ChatUser> getUsers()
    {
        return users;
    }

    private void fireUserListeners( ChatUser user, ChangeType change )
    {
        for( IUserListener l : userListeners )
        {
            l.userChanged( user, change );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public final void addUser( ChatUser user )
    {
        users.add( user );
        fireUserListeners( user, ChangeType.ADDED );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void removeUser( ChatUser user )
    {
        users.remove( user );
        fireUserListeners( user, ChangeType.REMOVED );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setUserAvailable( ChatUser user, boolean available )
    {
        int index = users.indexOf( user );
        if( index > -1 )
        {
            users.remove( index );
            users.add( index, user );
            fireUserListeners( user, ChangeType.UPDATED );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public final boolean isUserParticipating( ChatUser user )
    {
        return users.contains( user );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public final void leaveConversation()
    {
        chat.removeConversation( this );
        chat = null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void receiveMessage( ChatMessage message )
    {
        messageReceivedListeners.fireListeners( this, message );
    }
}
