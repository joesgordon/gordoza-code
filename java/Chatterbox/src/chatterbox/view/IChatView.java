package chatterbox.view;

import java.util.List;

import org.jutils.ui.event.ItemActionListener;

import chatterbox.model.*;

public interface IChatView
{
    public void setChat( IChatRoom chat );

    public void addDisplayNameChangedListener( ItemActionListener<String> l );

    public void addConversationStartedListener(
        ItemActionListener<List<IUser>> l );

    public IConversationView getDefaultConversationView();

    public IConversationView createConversationView( IConversation conversation );
}
