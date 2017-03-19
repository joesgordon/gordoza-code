package chatterbox.data;

import org.jutils.Utils;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatUser
{
    /** The name of the user on the system (e.g. user@system). */
    public final String userId;
    /** The name the user has chosen for their default. */
    public String displayName;
    // TODO make nick name usable
    /** The name the local user has chosen for the remote user. */
    public String nickName;
    /** {@code true} if the user has been active recently. */
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
        this.displayName = displayName;
        this.nickName = displayName;
        this.available = true;
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
