package org.cc.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 * 
 ******************************************************************************/
public class VersioningSystem
{
    /**  */
    private String version;
    /**  */
    private LockInfo lockInfo;
    /**  */
    private String defaultRepository;
    /**  */
    private List<Repository> repositories;

    /***************************************************************************
     * 
     **************************************************************************/
    public VersioningSystem()
    {
        defaultRepository = null;
        lockInfo = null;
        repositories = new ArrayList<Repository>();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String getVersion()
    {
        return version;
    }

    /***************************************************************************
     * @param version
     **************************************************************************/
    public void setVersion( String version )
    {
        this.version = version;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public String getDefaultRepository()
    {
        return defaultRepository;
    }

    /***************************************************************************
     * @param repo
     **************************************************************************/
    public void setDefaultRepository( String repo )
    {
        defaultRepository = repo;
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
    public List<Repository> getRepositories()
    {
        return repositories;
    }

    /***************************************************************************
     * @param repos
     **************************************************************************/
    public void setRepostories( List<Repository> repos )
    {
        repositories.clear();
        repositories.addAll( repos );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public List<Baseline> getBaselines()
    {
        List<Baseline> baselines = new ArrayList<Baseline>();
        for( Repository r : repositories )
        {
            baselines.addAll( r.getBaselines() );
        }
        return baselines;
    }

    /***************************************************************************
     * Returns a list of all sandboxes in the system.
     **************************************************************************/
    public List<Sandbox> getSandboxes()
    {
        List<Sandbox> baselines = new ArrayList<Sandbox>();
        for( Repository r : repositories )
        {
            for( Baseline b : r.getBaselines() )
            {
                for( OpenTask ot : b.getOpenTasks() )
                {
                    baselines.addAll( ot.getSandboxes() );
                }
            }
        }
        return baselines;
    }

    /***************************************************************************
     * @param location
     **************************************************************************/
    public OpenTask getOpenTaskBySandbox( File location )
    {
        OpenTask ot = null;

        for( Repository r : repositories )
        {
            ot = r.getOpenTaskBySandbox( location );
            if( ot != null )
            {
                break;
            }
        }

        return ot;
    }
}
