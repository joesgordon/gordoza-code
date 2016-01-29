package org.jutils;

import java.util.ArrayList;
import java.util.List;

/***************************************************************************
 * 
 **************************************************************************/
public interface IMemberAccessor<T, M>
{
    public M get( T item );

    public static <T, M> List<M> getMemberList( List<T> items,
        IMemberAccessor<T, M> ima )
    {
        List<M> members = new ArrayList<>( items.size() );

        for( T item : items )
        {
            members.add( ima.get( item ) );
        }

        return members;
    }
}
