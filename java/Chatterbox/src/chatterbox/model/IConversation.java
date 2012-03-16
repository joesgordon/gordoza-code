package chatterbox.model;

import java.util.List;

import org.jutils.ui.event.ItemActionListener;

public interface IConversation
{
    public String getConversationId();

    public void sendMessage( IChatMessage message );

    public void receiveMessage( IChatMessage message );

    public void leaveConversation();

    public List<IUser> getRecipients();

    public IChat getChat();

    public List<IUser> getUsers();

    public void addUser( IUser user );

    public void removeUser( IUser user );

    public void setUserAvailable( IUser user, boolean available );

    public boolean isUserParticipating( IUser user );

    public void addMessageReceivedListener( ItemActionListener<IChatMessage> l );

    public void addUserAddedListener( ItemActionListener<IUser> l );

    public void addUserAvailableListener( ItemActionListener<IUser> l );

    public void addUserUnavailableListener( ItemActionListener<IUser> l );

    public void addUserRemovedListener( ItemActionListener<IUser> l );
}
