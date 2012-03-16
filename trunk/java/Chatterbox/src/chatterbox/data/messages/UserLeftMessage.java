package chatterbox.data.messages;

import chatterbox.model.IUser;

public class UserLeftMessage
{
    private String conversationId;
    private IUser user;

    public UserLeftMessage( String conversationId, IUser user )
    {
        this.conversationId = conversationId;
        this.user = user;
    }

    public String getConversationId()
    {
        return conversationId;
    }

    public IUser getUser()
    {
        return user;
    }
}
