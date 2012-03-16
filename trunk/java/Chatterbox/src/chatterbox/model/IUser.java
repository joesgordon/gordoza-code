package chatterbox.model;

import org.jutils.ui.event.ItemActionListener;

public interface IUser
{
    public String getUserId();

    public String getDisplayName();

    public void setDisplayName( String name );

    public boolean isAvailable();

    public void setAvailable( boolean available );

    public void addChangedListener( ItemActionListener<IUser> l );

    @Override
    public int hashCode();

    @Override
    public boolean equals( Object userObject );
}
