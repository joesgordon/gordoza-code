package chatterbox.ui;

import java.util.List;

import javax.swing.JComponent;

import org.jutils.ui.event.ItemActionList;
import org.jutils.ui.event.ItemActionListener;
import org.jutils.ui.model.IView;

import chatterbox.model.*;
import chatterbox.view.IChatView;
import chatterbox.view.IConversationView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatView implements IChatView, IView<JComponent>
{
    /**  */
    private final ConversationView conversationPanel;

    /**  */
    private final ItemActionList<List<IUser>> conversationStartedListeners;
    /**  */
    private final ItemActionList<String> displayNameChangedListeners;

    /**  */
    private IChat chat;

    /***************************************************************************
     * 
     **************************************************************************/
    public ChatView()
    {
        this.conversationPanel = new ConversationView( this );

        this.conversationStartedListeners = new ItemActionList<List<IUser>>();
        this.displayNameChangedListeners = new ItemActionList<String>();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IChat getChat()
    {
        return chat;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setChat( IChat chat )
    {
        this.chat = chat;

        conversationPanel.setConversation( chat.getDefaultConversation() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IConversationView createConversationView( IConversation conversation )
    {
        return new ConversationFrame( this, conversation );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IConversationView getDefaultConversationView()
    {
        return conversationPanel;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addConversationStartedListener(
        ItemActionListener<List<IUser>> l )
    {
        conversationStartedListeners.addListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addDisplayNameChangedListener( ItemActionListener<String> l )
    {
        displayNameChangedListeners.addListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return conversationPanel;
    }
}
