package chatterbox.messenger;

import java.io.IOException;
import java.util.*;

import org.jutils.ValidationException;
import org.jutils.ui.event.ItemActionList;
import org.jutils.ui.event.ItemActionListener;

import chatterbox.data.ChatInfo;
import chatterbox.data.ChatUser;
import chatterbox.data.messages.ChatMessage;
import chatterbox.model.ChangeType;
import chatterbox.model.IUserListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatHandler
{
    /**  */
    public final ChatInfo info;
    /**  */
    private ChatterboxHandler chatterbox;
    /**  */
    private List<ChatUser> users;

    /**  */
    protected ItemActionList<ChatMessage> messageReceivedListeners;

    /**  */
    protected List<IUserListener> userListeners;

    /***************************************************************************
     * @param chat
     * @param info
     **************************************************************************/
    public ChatHandler( ChatterboxHandler chat, ChatInfo info )
    {
        this.info = info;
        this.chatterbox = chat;
        this.users = new Vector<ChatUser>();

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
    public void sendMessage( ChatMessage message )
    {
        try
        {
            chatterbox.sendMessage( message );
        }
        catch( IOException ex )
        {
            throw new RuntimeException( ex );
        }
        catch( ValidationException ex )
        {
            throw new RuntimeException( ex );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public final void leaveConversation()
    {
        chatterbox.removeConversation( this );
        chatterbox = null;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public ChatUser getLocalUser()
    {
        return chatterbox.getLocalUser();
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
    public final ChatterboxHandler getChat()
    {
        return chatterbox;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public final String getConversationId()
    {
        return info.id;
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
    public void setUserAvailable( ChatUser user )
    {
        int index = users.indexOf( user );
        if( index > -1 )
        {
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
