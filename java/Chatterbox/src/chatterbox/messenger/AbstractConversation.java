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
public abstract class AbstractConversation
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
    public final void addMessageReceivedListener(
        ItemActionListener<ChatMessage> l )
    {
        messageReceivedListeners.addListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void addUserListener( IUserListener l )

    {
        userListeners.add( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public final Chat getChat()
    {
        return chat;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public final String getConversationId()
    {
        return localId;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public final List<ChatUser> getUsers()
    {
        return users;
    }

    /***************************************************************************
     * @param user
     * @param change
     **************************************************************************/
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
    public final void addUser( ChatUser user )
    {
        users.add( user );
        fireUserListeners( user, ChangeType.ADDED );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void removeUser( ChatUser user )
    {
        users.remove( user );
        fireUserListeners( user, ChangeType.REMOVED );
    }

    /***************************************************************************
     * 
     **************************************************************************/
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
    public final boolean isUserParticipating( ChatUser user )
    {
        return users.contains( user );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void receiveMessage( ChatMessage message )
    {
        messageReceivedListeners.fireListeners( this, message );
    }
}
