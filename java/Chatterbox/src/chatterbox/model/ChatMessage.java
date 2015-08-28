package chatterbox.model;

import java.util.List;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatMessage
{
    /**  */
    public final String conversation;
    /**  */
    public final IUser sender;
    /**  */
    public final long txTime;
    /**  */
    public final long rxTime;
    /**  */
    public final String text;
    /**  */
    public final List<MessageAttributeSet> attributes;

    /***************************************************************************
     * @param conversation
     * @param sender
     * @param tx
     * @param rx
     * @param text
     * @param attributes
     **************************************************************************/
    public ChatMessage( String conversation, IUser sender, long tx, long rx,
        String text, List<MessageAttributeSet> attributes )
    {
        this.conversation = conversation;
        this.txTime = tx;
        this.rxTime = rx;
        this.sender = sender;
        this.text = text;
        this.attributes = attributes;
    }
}
