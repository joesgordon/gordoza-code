package chatterbox.data.messages;

import chatterbox.data.ChatUser;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UserAvailableMessage
{
    /**  */
    public final ChatUser user;

    /***************************************************************************
     * @param user
     **************************************************************************/
    public UserAvailableMessage( ChatUser user )
    {
        this.user = new ChatUser( user );
    }
}
