package chatterbox.controller;

import java.util.ArrayList;
import java.util.List;

import org.jutils.ui.event.ItemActionEvent;
import org.jutils.ui.event.ItemActionListener;

import chatterbox.model.*;
import chatterbox.view.IChatView;
import chatterbox.view.IConversationView;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatController
{
    /**  */
    private IChatView chatView;
    /**  */
    private IChatRoom chatModel;

    private List<IConversationView> conversationViews;

    /***************************************************************************
     * @param view
     * @param room
     **************************************************************************/
    public ChatController( IChatRoom room, IChatView view )
    {
        chatView = view;
        chatModel = room;
        conversationViews = new ArrayList<IConversationView>();

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
                IConversationView view = null;
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

                view.showView();
            }
        };

        chatModel.addConversationCreatedListener( convCreatedListener );

        chatView.addDisplayNameChangedListener( userChangedListener );
        chatView.addConversationStartedListener( conversationStartedListener );

        new ConversationController( chatModel.getDefaultConversation(),
            chatView.getDefaultConversationView() );
    }

    /***************************************************************************
     * @param user
     * @return
     **************************************************************************/
    protected IConversationView containsConversation( IUser user )
    {
        for( IConversationView view : conversationViews )
        {
            List<IUser> users = view.getConversation().getUsers();
            if( users.size() == 1 && users.get( 0 ).equals( user ) )
            {
                return view;
            }
        }
        return null;
    }
}
