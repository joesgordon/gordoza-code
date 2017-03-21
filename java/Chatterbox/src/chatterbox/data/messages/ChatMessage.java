package chatterbox.data.messages;

import chatterbox.data.DecoratedText;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatMessage
{
    /**  */
    public final String chatId;
    /**  */
    public final String sender;
    /**  */
    public final long txTime;
    /**  */
    public final long rxTime;
    /**  */
    public final DecoratedText text;

    /***************************************************************************
     * @param chatId
     * @param sender
     * @param tx
     * @param rx
     * @param text
     * @param attributes
     **************************************************************************/
    public ChatMessage( String chatId, String sender, long tx, long rx,
        DecoratedText text )
    {
        this.chatId = chatId;
        this.txTime = tx;
        this.rxTime = rx;
        this.sender = sender;
        this.text = text;
    }
}
