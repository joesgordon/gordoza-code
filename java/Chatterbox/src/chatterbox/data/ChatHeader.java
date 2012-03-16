package chatterbox.data;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatHeader
{
    /**  */
    private ChatMessageType messageType;
    /**  */
    private int messageLength;

    /***************************************************************************
     * @param messageType
     * @param messageLength
     **************************************************************************/
    public ChatHeader( short messageType, int messageLength )
    {
        this( ChatMessageType.fromShort( messageType ), messageLength );
    }

    /***************************************************************************
     * @param messageType
     * @param messageLength
     **************************************************************************/
    public ChatHeader( ChatMessageType messageType, int messageLength )
    {
        this.messageType = messageType;
        this.messageLength = messageLength;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public ChatMessageType getMessageType()
    {
        return messageType;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public int getMessageLength()
    {
        return messageLength;
    }
}
