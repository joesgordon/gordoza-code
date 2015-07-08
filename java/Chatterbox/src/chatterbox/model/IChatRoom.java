package chatterbox.model;

import java.io.IOException;
import java.util.List;

import org.jutils.ui.event.ItemActionListener;

public interface IChatRoom
{
    public void connect( String group, int port ) throws IOException;

    public void disconnect();

    public String getAddress();

    public int getPort();

    public void removeConversation( IConversation conversation );

    public void addConversationCreatedListener(
        ItemActionListener<IConversation> l );

    public IConversation getDefaultConversation();

    public IConversation createConversation( List<IUser> users );

    public IUser getLocalUser();
}
