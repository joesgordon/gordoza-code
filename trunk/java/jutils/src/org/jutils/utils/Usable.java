package org.jutils.utils;

public class Usable<T>
{
    public boolean isUsed;
    public T data;

    public Usable()
    {
        this( true );
    }

    public Usable( boolean isUsed )
    {
        this.isUsed = isUsed;
    }

    public Usable( Usable<T> usable )
    {
        if( usable == null )
        {
            this.isUsed = false;
            this.data = null;
        }
        else
        {
            this.isUsed = usable.isUsed;
            this.data = usable.data;
        }
    }
}
