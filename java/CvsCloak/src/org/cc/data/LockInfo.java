package org.cc.data;

/**
 * 
 */
public class LockInfo
{
    /**  */
    private String reason;
    /**  */
    private String user;
    /**
     * Number of seconds from January 1, 1970 @ 00:00. Java's time is normally
     * milliseconds from the same point in time.
     */
    private long time;

    /**
     * 
     */
    public LockInfo()
    {
        reason = "Cause";
        user = System.getProperty( "user.name" );
    }

    public String getUser()
    {
        return user;
    }

    public void setUser( String user )
    {
        this.user = user;
    }

    public String getReason()
    {
        return reason;
    }

    public void setReason( String r )
    {
        reason = r;
    }

    public long getTime()
    {
        return time;
    }

    /**
     * Sets the number of seconds since epoch.
     * @param t
     */
    public void setTime( long t )
    {
        time = t;
    }
}
