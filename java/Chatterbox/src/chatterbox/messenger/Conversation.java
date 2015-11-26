package chatterbox.messenger;

import java.io.IOException;
import java.util.List;

import org.jutils.ValidationException;

import chatterbox.model.ChatMessage;
import chatterbox.model.IUser;

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
    public Conversation( Chat chat, String id, List<IUser> users )
    {
        super( chat, id, users );

        this.chat = chat;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
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
}
