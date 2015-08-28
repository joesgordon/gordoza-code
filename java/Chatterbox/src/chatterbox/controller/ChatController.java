package chatterbox.controller;

import java.util.ArrayList;
import java.util.List;

import org.jutils.ui.event.ItemActionEvent;
import org.jutils.ui.event.ItemActionListener;

import chatterbox.model.*;
import chatterbox.ui.ChatView;
import chatterbox.ui.ConversationView;

/*******************************************************************************
 *  
 ******************************************************************************/
public class ChatController
{
    /**  */
    private ChatView chatView;
    /**  */
    private IChat chatModel;

    private List<ConversationView> conversationViews;

    /***************************************************************************
     * @param view
     * @param chat
     **************************************************************************/
    public ChatController( IChat chat, ChatView view )
    {
        chatView = view;
        chatModel = chat;
        conversationViews = new ArrayList<>();

        ItemActionListener<IConversation> convCreatedListener;
        ItemActionListener<String> userChangedListener;
        ItemActionListener<List<IUser>> conversationStartedListener;

        convCreatedListener = new ItemActionListener<IConversation>()
        {
            @Override
            public void actionPerformed( ItemActionEvent<IConversation> event )
            {
                chatView.createConversationView( event.getItem() );
            }
        };

        userChangedListener = new ItemActionListener<String>()
        {
            @Override
            public void actionPerformed( ItemActionEvent<String> event )
            {
                chatModel.getLocalUser().setDisplayName( event.getItem() );
            }
        };

        conversationStartedListener = new ItemActionListener<List<IUser>>()
        {
            @Override
            public void actionPerformed( ItemActionEvent<List<IUser>> event )
            {
                List<IUser> users = event.getItem();
                ConversationView view = null;
                IConversation conv;

                if( users.size() == 1 )
                {
                    view = containsConversation( users.get( 0 ) );
                }

                if( view == null )
                {
                    conv = chatModel.createConversation( users );
                    if( conv != null )
                    {
                        view = chatView.createConversationView( conv );
                        new ConversationController( conv, view );
                        conversationViews.add( view );
                    }
                    else
                    {
                        return;
                    }
                }
            }
        };

        chatModel.addConversationCreatedListener( convCreatedListener );

        new ConversationController( chatModel.getDefaultConversation(),
            chatView.getDefaultConversationView() );
    }

    /***************************************************************************
     * @param user
     * @return
     **************************************************************************/
    protected ConversationView containsConversation( IUser user )
    {
        for( ConversationView view : conversationViews )
        {
            List<IUser> users = view.getData().getUsers();
            if( users.size() == 1 && users.get( 0 ).equals( user ) )
            {
                return view;
            }
        }

        return null;
    }
}
