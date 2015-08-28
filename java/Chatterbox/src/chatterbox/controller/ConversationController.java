package chatterbox.controller;

import chatterbox.model.IConversation;
import chatterbox.ui.ConversationView;

/**
 * 
 */
public class ConversationController
{
    /**  */
    private IConversation conversationModel;
    /**  */
    private ConversationView conversationView;

    /**
     * @param model
     * @param view
     */
    public ConversationController( IConversation model, ConversationView view )
    {
        conversationModel = model;
        conversationView = view;
    }
}
