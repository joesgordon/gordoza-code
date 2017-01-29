package chatterbox.model;

import chatterbox.data.ChatUser;
import chatterbox.data.DecoratedText;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatMessage
{
    /**  */
    public final String conversation;
    /**  */
    public final ChatUser sender;
    /**  */
    public final long txTime;
    /**  */
    public final long rxTime;
    /**  */
    public final DecoratedText text;

    /***************************************************************************
     * @param conversation
     * @param sender
     * @param tx
     * @param rx
     * @param text
     * @param attributes
     **************************************************************************/
    public ChatMessage( String conversation, ChatUser sender, long tx, long rx,
        DecoratedText text )
    {
        this.conversation = conversation;
        this.txTime = tx;
        this.rxTime = rx;
        this.sender = sender;
        this.text = text;
    }
}
