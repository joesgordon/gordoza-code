package org.cojo.data;

import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 *
 ******************************************************************************/
public class Project
{
    /**  */
    public String name;
    /**  */
    public final List<User> users;
    /**  */
    public final List<ChangeRequest> changes;

    /***************************************************************************
     * 
     **************************************************************************/
    public Project()
    {
        this.name = "";
        this.users = new ArrayList<>();
        this.changes = new ArrayList<>();
    }

    /***************************************************************************
     * @param name
     * @return
     **************************************************************************/
    public User createUser( String name )
    {
        User u = new User( users.size(), name );

        users.add( u );

        return u;
    }

    /***************************************************************************
     * @param id
     * @return
     **************************************************************************/
    public User getUser( int id )
    {
        for( User u : users )
        {
            if( u.userId == id )
            {
                return u;
            }
        }

        throw new IllegalArgumentException(
            "No user with id " + id + " exists" );
    }
}
