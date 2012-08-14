package org.cojo.model;

public interface IFinding
{
    public int getNumber();

    public IUser getUser();

    public long getDate();

    public boolean isAccepted();

    public String getDescription();

    public String getComments();
}
