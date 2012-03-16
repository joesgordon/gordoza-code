package chatterbox.data;

/*******************************************************************************
 * 
 ******************************************************************************/
public enum ChatMessageType
{
    Chat, UserAvailable, UserLeft;

    /***************************************************************************
     * @return
     **************************************************************************/
    public short toShort()
    {
        switch( this )
        {
            case Chat:
                return 0;
            case UserAvailable:
                return 1;
            case UserLeft:
                return 2;
        }

        throw new RuntimeException( "Unknown message type: " + this );
    }

    /***************************************************************************
     * @param type
     * @return
     **************************************************************************/
    public static ChatMessageType fromShort( short type )
    {
        switch( type )
        {
            case 0:
                return Chat;
            case 1:
                return UserAvailable;
            case 2:
                return UserLeft;
        }

        throw new RuntimeException( "Unknown message type: " + type );
    }
}
