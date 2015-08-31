package chatterbox.data.messages;

import chatterbox.model.IUser;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UserAvailableMessage
{
    /**  */
    public final IUser user;

    /***************************************************************************
     * @param user
     **************************************************************************/
    public UserAvailableMessage( IUser user )
    {
        this.user = user;
    }
}
