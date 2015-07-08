package chatterbox.messenger;

import java.io.IOException;
import java.util.TimerTask;

import chatterbox.data.messages.UserAvailableMessage;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UserAvailableTask extends TimerTask
{
    /**  */
    private ChatRoom chat;
    /**  */
    private UserAvailableMessage message;

    /*******************************************************************************
     * @param chat
     ******************************************************************************/
    public UserAvailableTask( ChatRoom chat )
    {
        this.chat = chat;
        message = new UserAvailableMessage( chat.getLocalUser() );
    }

    /*******************************************************************************
     * 
     ******************************************************************************/
    @Override
    public void run()
    {
        try
        {
            chat.sendMessage( message );
        }
        catch( IOException ex )
        {
            // TODO Maybe do something else here
            ex.printStackTrace();
        }
    }
}
