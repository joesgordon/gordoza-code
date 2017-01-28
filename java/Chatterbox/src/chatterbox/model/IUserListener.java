package chatterbox.model;

import chatterbox.data.ChatUser;

/*******************************************************************************
 * 
 ******************************************************************************/
public interface IUserListener
{
    /***************************************************************************
     * @param user
     * @param change
     **************************************************************************/
    public void userChanged( ChatUser user, ChangeType change );
}
