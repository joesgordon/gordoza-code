package chatterbox.ui;

import javax.swing.JComponent;

import org.jutils.ui.model.IView;

import chatterbox.messenger.Chat;
import chatterbox.model.IConversation;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatView implements IView<JComponent>
{
    /**  */
    private final ConversationView conversationPanel;

    /**  */
    private Chat chat;

    /***************************************************************************
     * 
     **************************************************************************/
    public ChatView( Chat chat )
    {
        this.chat = chat;

        this.conversationPanel = new ConversationView( chat );

        conversationPanel.setData( chat.getDefaultConversation() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public Chat getChat()
    {
        return chat;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public ConversationView createConversationView( IConversation conversation )
    {
        ConversationView cv = new ConversationView( chat );

        cv.setData( conversation );

        return cv;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public ConversationView getDefaultConversationView()
    {
        return conversationPanel;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JComponent getView()
    {
        return conversationPanel.getView();
    }
}
