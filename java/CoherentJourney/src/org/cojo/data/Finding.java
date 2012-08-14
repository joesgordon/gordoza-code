package org.cojo.data;

import org.cojo.model.IFinding;
import org.cojo.model.IUser;

public class Finding implements IFinding
{
    private int number;
    private IUser user;
    private long date;
    private String description;
    private boolean accepted;
    private String comments;

    public Finding( int number, IUser user, long date, String description,
        boolean accepted, String comments )
    {
        this.number = number;
        this.user = user;
        this.date = date;
        this.description = description;
        this.accepted = accepted;
        this.comments = comments;
    }

    @Override
    public String getComments()
    {
        return comments;
    }

    @Override
    public long getDate()
    {
        return date;
    }

    @Override
    public String getDescription()
    {
        return description;
    }

    @Override
    public int getNumber()
    {
        return number;
    }

    @Override
    public IUser getUser()
    {
        return user;
    }

    @Override
    public boolean isAccepted()
    {
        return accepted;
    }
}
