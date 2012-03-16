package chatterbox.view;

import java.util.List;

import org.jutils.ui.event.ItemActionListener;

import chatterbox.model.*;

public interface IConversationView
{
    public void setConversation( IConversation conversation );

    public IConversation getConversation();

    public void addConversationStartedListener(
        ItemActionListener<List<IUser>> l );

    public void addMessageSentListener( ItemActionListener<IChatMessage> l );

    public void addUserChangedListener( ItemActionListener<String> l );

    public void addConversationLeftListener( ItemActionListener<Object> l );

    public void showView();

    public IChatView getChatView();
}
