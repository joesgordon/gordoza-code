package chatterbox.data;

import org.jutils.ui.event.ItemActionList;
import org.jutils.ui.event.ItemActionListener;

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

    /**  */
    private ItemActionList<IUser> userChangedListners;

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
        this.userChangedListners = new ItemActionList<IUser>();
        this.available = true;
        setDisplayName( displayName );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addChangedListener( ItemActionListener<IUser> l )
    {
        userChangedListners.addListener( l );
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
        userChangedListners.fireListeners( this, this );
    }

    /***************************************************************************
     * @param available
     **************************************************************************/
    public void setAvailable( boolean available )
    {
        this.available = available;
        userChangedListners.fireListeners( this, this );
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
    public boolean equals( Object userObject )
    {
        return userId.equals( ( ( IUser )userObject ).getUserId() );
    }
}
