package chatterbox.data;

import java.time.LocalDateTime;

import org.jutils.core.Utils;

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
    /** {@code false} if the user does not want to be disturbed. */
    public boolean available;
    /** {@code false} if the user has been active recently. */
    public boolean away;
    /**  */
    public LocalDateTime lastSeen;

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
        this.away = false;
        this.lastSeen = LocalDateTime.MIN;
    }

    /***************************************************************************
     * @param user
     **************************************************************************/
    public ChatUser( ChatUser user )
    {
        this.userId = user.userId;
        this.displayName = user.displayName;
        this.nickName = user.nickName;
        this.available = user.available;
        this.away = user.away;
        this.lastSeen = user.lastSeen;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public String toString()
    {
        return Utils.argsToString( userId, displayName, nickName, available );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public int hashCode()
    {
        return displayName.hashCode();
    }

    /***************************************************************************
     * {@inheritDoc}
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
