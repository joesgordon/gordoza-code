package chatterbox.view;

import java.util.List;

import org.jutils.ui.event.ItemActionListener;

import chatterbox.model.*;

public interface IChatView
{
    public IChat getChat();

    public void setChat( IChat chat );

    public void addDisplayNameChangedListener( ItemActionListener<String> l );

    public void addConversationStartedListener(
        ItemActionListener<List<IUser>> l );

    public IConversationView getDefaultConversationView();

    public IConversationView createConversationView( IConversation conversation );
}
