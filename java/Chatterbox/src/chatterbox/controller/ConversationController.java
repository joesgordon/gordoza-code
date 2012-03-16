package chatterbox.controller;

import java.awt.Component;
import java.util.List;

import javax.swing.JOptionPane;

import org.jutils.ui.event.ItemActionEvent;
import org.jutils.ui.event.ItemActionListener;

import chatterbox.model.*;
import chatterbox.view.IConversationView;

/**
 * 
 */
public class ConversationController
{
    /**  */
    private IConversation conversationModel;
    /**  */
    private IConversationView conversationView;

    /**
     * @param model
     * @param view
     */
    public ConversationController( IConversation model, IConversationView view )
    {
        conversationModel = model;
        conversationView = view;

        ItemActionListener<IChatMessage> messageSentListener;
        ItemActionListener<String> userChangedListener;
        ItemActionListener<List<IUser>> conversationStartedListener;

        messageSentListener = new ItemActionListener<IChatMessage>()
        {
            @Override
            public void actionPerformed( ItemActionEvent<IChatMessage> event )
            {
                try
                {
                    conversationModel.sendMessage( event.getItem() );
                }
                catch( Exception ex )
                {
                    JOptionPane.showMessageDialog(
                        ( Component )conversationView, "", "",
                        JOptionPane.ERROR_MESSAGE );
                }
            }
        };

        userChangedListener = new ItemActionListener<String>()
        {
            @Override
            public void actionPerformed( ItemActionEvent<String> event )
            {
                conversationModel.getChat().getLocalUser().setDisplayName(
                    event.getItem() );
            }
        };

        conversationStartedListener = new ItemActionListener<List<IUser>>()
        {
            @Override
            public void actionPerformed( ItemActionEvent<List<IUser>> event )
            {
                IConversation newConv = conversationModel.getChat().createConversation(
                    event.getItem() );
                IConversationView newView = conversationView.getChatView().createConversationView(
                    newConv );

                new ConversationController( newConv, newView );

                newView.showView();
            }
        };

        conversationView.addConversationStartedListener( conversationStartedListener );
        conversationView.addMessageSentListener( messageSentListener );
        conversationView.addUserChangedListener( userChangedListener );
    }
}
