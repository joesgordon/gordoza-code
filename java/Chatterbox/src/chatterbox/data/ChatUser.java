package chatterbox.data;

import org.jutils.Utils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatUser
{
    /** The name of the user on the system. */
    public final String userId;
    /**  */
    public String displayName;
    /**  */
    public String nickName;
    /**  */
    public boolean available;

    /***************************************************************************
     * @param userId
     **************************************************************************/
    public ChatUser( String userId )
    {
        this( userId, userId );
    }

    /***************************************************************************
     * @param userId
     * @param displayName
     **************************************************************************/
    public ChatUser( String userId, String displayName )
    {
        this.userId = userId;
        this.available = true;
        this.displayName = displayName;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String toString()
    {
        return Utils.argsToString( userId, displayName, nickName, available );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int hashCode()
    {
        return displayName.hashCode();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean equals( Object obj )
    {
        if( obj == null )
        {
            return false;
        }
        else if( obj instanceof ChatUser )
        {
            ChatUser u = ( ChatUser )obj;

            return userId.equals( u.userId );// &&
            // displayName.equals( u.getDisplayName() );
        }

        return false;
    }
}
