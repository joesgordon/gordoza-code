package chatterbox.data.messages;

import chatterbox.model.IUser;

public class UserAvailableMessage
{
    private IUser user;

    public UserAvailableMessage( IUser user )
    {
        this.user = user;
    }

    public IUser getUser()
    {
        return user;
    }
}
