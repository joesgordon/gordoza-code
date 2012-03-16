package org.cc.model;

public interface IFinding
{
    public int getNumber();

    public IUser getUser();

    public long getDate();

    public boolean isAccepted();

    public String getDescription();

    public String getComments();
}
