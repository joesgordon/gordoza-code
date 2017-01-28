package chatterbox.data.messages;

import chatterbox.data.ChatUser;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UserLeftMessage
{
    /**  */
    public final String conversationId;
    /**  */
    public final ChatUser user;

    /***************************************************************************
     * @param conversationId
     * @param user
     **************************************************************************/
    public UserLeftMessage( String conversationId, ChatUser user )
    {
        this.conversationId = conversationId;
        this.user = user;
    }
}
