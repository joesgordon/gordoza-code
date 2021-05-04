package chatterbox.messenger;

import java.io.IOException;
import java.util.TimerTask;

import org.jutils.core.ValidationException;

import chatterbox.data.messages.UserAvailableMessage;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UserAvailableTask extends TimerTask
{
    /**  */
    private ChatterboxHandler chat;
    /**  */
    private UserAvailableMessage message;

    /***************************************************************************
     * @param chat
     **************************************************************************/
    public UserAvailableTask( ChatterboxHandler chat )
    {
        this.chat = chat;
        message = new UserAvailableMessage( chat.getLocalUser() );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void run()
    {
        try
        {
            // LogUtils.printDebug(
            // "Sending available: " + chat.getLocalUser().getUserId() );
            chat.sendMessage( message );
        }
        catch( IOException | ValidationException ex )
        {
            // TODO Maybe do something else here
            ex.printStackTrace();
        }
    }
}
