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
    public final List<ProjectUser> users;
    /**  */
    public final List<Task> tasks;

    /***************************************************************************
     * @param currentUser
     **************************************************************************/
    public Project()
    {
        this.name = "";
        this.users = new ArrayList<>();
        this.tasks = new ArrayList<>();
    }

    /***************************************************************************
     * @param id
     * @return
     **************************************************************************/
    public ProjectUser getUser( int id )
    {
        for( ProjectUser u : users )
        {
            if( u.id == id )
            {
                return u;
            }
        }

        throw new IllegalArgumentException(
            "No user with id " + id + " exists" );
    }
}
