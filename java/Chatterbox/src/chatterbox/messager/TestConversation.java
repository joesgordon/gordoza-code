package chatterbox.messager;

import java.util.*;

import chatterbox.data.DefaultMessage;
import chatterbox.model.*;

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
    public TestConversation( IChat chat, String id, List<IUser> users )
    {
        super( chat, id, users );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void sendMessage( IChatMessage message )
    {
        messageReceivedListeners.fireListeners( this, message );

        // Pretend that the remote user responded.
        IChatMessage retort = new DefaultMessage( getRandomUser(), "Polo: " +
            message.getText(), new ArrayList<IMessageAttributeSet>(),
            new Date(), message.getConversationId(), false );
        messageReceivedListeners.fireListeners( this, retort );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private IUser getRandomUser()
    {
        List<IUser> users = getUsers();
        int index = ( int )Math.floor( Math.random() * users.size() );

        return users.get( index );
    }
}
