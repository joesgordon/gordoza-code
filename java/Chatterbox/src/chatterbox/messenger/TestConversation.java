package chatterbox.messenger;

import java.util.ArrayList;
import java.util.List;

import chatterbox.data.ChatUser;
import chatterbox.model.ChatMessage;
import chatterbox.model.IChat;

/***************************************************************************
 * 
 **************************************************************************/
public class TestConversation extends AbstractConversation
{
    /***************************************************************************
     * @param id
     * @param remoteId
     * @param chat
     * @param user
     **************************************************************************/
    public TestConversation( IChat chat, String id, List<ChatUser> users )
    {
        super( chat, id, users );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void sendMessage( ChatMessage message )
    {
        messageReceivedListeners.fireListeners( this, message );

        String text = "Polo: " + message.text;

        // Pretend that the remote user responded.
        ChatMessage retort = new ChatMessage( "", getRandomUser(), 0L, 0L, text,
            new ArrayList<>() );
        messageReceivedListeners.fireListeners( this, retort );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private ChatUser getRandomUser()
    {
        List<ChatUser> users = getUsers();
        int index = ( int )Math.floor( Math.random() * users.size() );

        return users.get( index );
    }
}
