package chatterbox.data;

import java.util.*;

import chatterbox.model.*;

public class DefaultMessage implements IChatMessage
{
    private IUser sender;
    private String text;
    private List<IMessageAttributeSet> attributeSets;
    private Date date;
    private String convId;
    private boolean localUser;

    public DefaultMessage( IUser sender, String text,
        List<IMessageAttributeSet> attributeSets, Date date, String convId,
        boolean localUser )
    {
        this.sender = sender;
        this.text = text;
        this.attributeSets = new ArrayList<IMessageAttributeSet>( attributeSets );
        this.date = date;
        this.convId = convId;
        this.localUser = localUser;
    }

    @Override
    public List<IMessageAttributeSet> getAttributeSets()
    {
        return attributeSets;
    }

    @Override
    public String getConversationId()
    {
        return convId;
    }

    @Override
    public String getText()
    {
        return text;
    }

    @Override
    public Date getTime()
    {
        return date;
    }

    @Override
    public boolean isLocalUser()
    {
        return localUser;
    }

    @Override
    public IUser getSender()
    {
        return sender;
    }
}
