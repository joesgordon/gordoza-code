package chatterbox.messenger;

import java.io.IOException;
import java.util.List;

import org.jutils.ValidationException;

import chatterbox.data.ChatUser;
import chatterbox.model.ChatMessage;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Conversation extends AbstractConversation
{
    /**  */
    private Chat chat;

    /***************************************************************************
     * @param id
     * @param remoteId
     * @param chat
     * @param users
     **************************************************************************/
    public Conversation( Chat chat, String id, List<ChatUser> users )
    {
        super( chat, id, users );

        this.chat = chat;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void sendMessage( ChatMessage message )
    {
        try
        {
            chat.sendMessage( message );
        }
        catch( IOException ex )
        {
            throw new RuntimeException( ex );
        }
        catch( ValidationException ex )
        {
            throw new RuntimeException( ex );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public final void leaveConversation()
    {
        chat.removeConversation( this );
        chat = null;
    }
}
