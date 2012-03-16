package org.cc.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Baseline
{
    /**  */
    private String name;
    /**  */
    private LockInfo lockInfo;
    /**  */
    private String updatingTask;
    /**  */
    private String lastClosedTask;
    /**  */
    private String startingRelease;
    /**  */
    private List<OpenTask> openTasks;
    /**  */
    private List<ClosedTask> closedTasks;

    /***************************************************************************
     * 
     **************************************************************************/
    public Baseline()
    {
        name = "";
        updatingTask = "";
        lockInfo = null;
        closedTasks = new ArrayList<ClosedTask>();
        openTasks = new ArrayList<OpenTask>();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String getName()
    {
        return name;
    }

    /***************************************************************************
     * @param name
     **************************************************************************/
    public void setName( String name )
    {
        this.name = name;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String getUpdatingTask()
    {
        return updatingTask;
    }

    /***************************************************************************
     * @param task
     **************************************************************************/
    public void setUpdatingTask( String task )
    {
        this.updatingTask = task;
    }

    public String getLastClosedTask()
    {
        return lastClosedTask;
    }

    public void setLastClosedTask( String task )
    {
        lastClosedTask = task;
    }

    public String getStartingRelease()
    {
        return startingRelease;
    }

    public void setStartingRelease( String release )
    {
        startingRelease = release;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public LockInfo getLockInfo()
    {
        return lockInfo;
    }

    /***************************************************************************
     * @param info
     **************************************************************************/
    public void setLockInfo( LockInfo info )
    {
        lockInfo = info;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public List<OpenTask> getOpenTasks()
    {
        return openTasks;
    }

    /***************************************************************************
     * @param tasks
     **************************************************************************/
    public void setOpenTasks( List<OpenTask> tasks )
    {
        openTasks = tasks;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public List<ClosedTask> getClosedTasks()
    {
        return closedTasks;
    }

    /***************************************************************************
     * @param tasks
     **************************************************************************/
    public void setClosedTasks( List<ClosedTask> tasks )
    {
        closedTasks.clear();
        closedTasks.addAll( tasks );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String toString()
    {
        return name;
    }

    public OpenTask getOpenTaskBySandbox( File location )
    {
        for( OpenTask t : openTasks )
        {
            if( t.containsSandbox( location ) )
            {
                return t;
            }
        }

        return null;
    }
}
