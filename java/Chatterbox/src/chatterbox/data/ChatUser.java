package chatterbox.data;

import chatterbox.model.IUser;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatUser implements IUser
{
    /**  */
    private final String userId;
    /**  */
    private String displayName;
    /**  */
    private boolean available;

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
    public String getDisplayName()
    {
        return displayName;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getUserId()
    {
        return userId;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void setDisplayName( String name )
    {
        displayName = name;
    }

    /***************************************************************************
     * @param available
     **************************************************************************/
    public void setAvailable( boolean available )
    {
        this.available = available;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isAvailable()
    {
        return available;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public String toString()
    {
        return getDisplayName();
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
        else if( obj instanceof IUser )
        {
            IUser u = ( IUser )obj;

            return userId.equals( u.getUserId() );// &&
            // displayName.equals( u.getDisplayName() );
        }

        return false;
    }
}
