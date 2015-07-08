package chatterbox.model;

import org.jutils.ui.event.ItemActionListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public interface IUser
{
    /***************************************************************************
     * @return
     **************************************************************************/
    public String getUserId();

    /***************************************************************************
     * @return
     **************************************************************************/
    public String getDisplayName();

    /***************************************************************************
     * @param name
     **************************************************************************/
    public void setDisplayName( String name );

    /***************************************************************************
     * @return
     **************************************************************************/
    public boolean isAvailable();

    /***************************************************************************
     * @param available
     **************************************************************************/
    public void setAvailable( boolean available );

    /***************************************************************************
     * @param l
     **************************************************************************/
    public void addChangedListener( ItemActionListener<IUser> l );

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int hashCode();

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean equals( Object userObject );
}
