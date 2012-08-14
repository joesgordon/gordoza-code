package org.cojo.data;

import org.cojo.model.IUser;

public class User implements IUser
{
    /**  */
    private String username;
    /**  */
    private String name;

    public User( String username, String name )
    {
        this.username = username;
        this.name = name;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getUsername()
    {
        return username;
    }

}
