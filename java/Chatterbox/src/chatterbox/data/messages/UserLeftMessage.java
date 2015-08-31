package chatterbox.data.messages;

import chatterbox.model.IUser;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UserLeftMessage
{
    /**  */
    public final String conversationId;
    /**  */
    public final IUser user;

    /***************************************************************************
     * @param conversationId
     * @param user
     **************************************************************************/
    public UserLeftMessage( String conversationId, IUser user )
    {
        this.conversationId = conversationId;
        this.user = user;
    }
}
