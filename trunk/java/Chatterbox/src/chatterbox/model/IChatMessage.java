package chatterbox.model;

import java.util.Date;
import java.util.List;

public interface IChatMessage
{
    public String getConversationId();

    public Date getTime();

    public IUser getSender();

    public String getText();

    public List<IMessageAttributeSet> getAttributeSets();

    public boolean isLocalUser();
}
