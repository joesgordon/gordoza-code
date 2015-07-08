package chatterbox.messenger;

import java.util.ArrayList;
import java.util.List;

import org.jutils.ui.event.ItemActionList;
import org.jutils.ui.event.ItemActionListener;

import chatterbox.model.*;

/***************************************************************************
 * 
 **************************************************************************/
public abstract class AbstractConversation implements IConversation
{
    /**  */
    private String localId;
    /**  */
    private IChat chat;
    /**  */
    private List<IUser> remoteUsers;

    /**  */
    protected ItemActionList<IChatMessage> messageReceivedListeners;

    /**  */
    protected ItemActionList<IUser> userAddedListeners;

    /**  */
    protected ItemActionList<IUser> userAvailableListeners;

    /**  */
    protected ItemActionList<IUser> userRemovedListeners;

    /**  */
    protected ItemActionList<IUser> userUnavailableListeners;

    /***************************************************************************
     * @param id
     * @param remoteId
     * @param chat
     * @param user
     **************************************************************************/
    public AbstractConversation( IChat chat, String id, List<IUser> users )
    {
        this.localId = id;
        this.chat = chat;
        this.remoteUsers = new ArrayList<IUser>();
        if( users != null )
        {
            this.remoteUsers.addAll( users );
        }

        userAddedListeners = new ItemActionList<IUser>();
        userAvailableListeners = new ItemActionList<IUser>();
        userRemovedListeners = new ItemActionList<IUser>();
        userUnavailableListeners = new ItemActionList<IUser>();
        messageReceivedListeners = new ItemActionList<IChatMessage>();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public final void addMessageReceivedListener(
        ItemActionListener<IChatMessage> l )
    {
        messageReceivedListeners.addListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public final void addUserAddedListener( ItemActionListener<IUser> l )
    {
        userAddedListeners.addListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public final void addUserAvailableListener( ItemActionListener<IUser> l )
    {
        userAvailableListeners.addListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public final void addUserRemovedListener( ItemActionListener<IUser> l )
    {
        userRemovedListeners.addListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public final void addUserUnavailableListener( ItemActionListener<IUser> l )
    {
        userUnavailableListeners.addListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public final IChat getChat()
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
    public final List<IUser> getRecipients()
    {
        return remoteUsers;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public final List<IUser> getUsers()
    {
        return remoteUsers;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public final void addUser( IUser user )
    {
        remoteUsers.add( user );
        userAddedListeners.fireListeners( this, user );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void removeUser( IUser user )
    {
        remoteUsers.remove( user );
        userRemovedListeners.fireListeners( this, user );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setUserAvailable( IUser user, boolean available )
    {
        if( remoteUsers.contains( user ) )
        {
            if( available )
            {
                userAvailableListeners.fireListeners( this, user );
            }
            else
            {
                userUnavailableListeners.fireListeners( this, user );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public final boolean isUserParticipating( IUser user )
    {
        return remoteUsers.contains( user );
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
    public final void receiveMessage( IChatMessage message )
    {
        messageReceivedListeners.fireListeners( this, message );
    }
}
