package org.cc.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 * 
 ******************************************************************************/
public class OpenTask
{
    /**  */
    private String name;
    /**  */
    private boolean approved;
    /**  */
    private List<Sandbox> sandboxes;
    /**  */
    private List<VersionedFile> files;

    /***************************************************************************
     * 
     **************************************************************************/
    public OpenTask()
    {
        name = "";
        approved = true;
        sandboxes = new ArrayList<Sandbox>();
        files = new ArrayList<VersionedFile>();
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
    public boolean isApproved()
    {
        return approved;
    }

    /***************************************************************************
     * @param b
     **************************************************************************/
    public void setApproved( boolean b )
    {
        approved = b;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public List<Sandbox> getSandboxes()
    {
        return sandboxes;
    }

    /***************************************************************************
     * @param files
     **************************************************************************/
    public void setSandboxes( List<Sandbox> list )
    {
        sandboxes = list;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public List<VersionedFile> getFiles()
    {
        return files;
    }

    /***************************************************************************
     * @param files
     **************************************************************************/
    public void setFiles( List<VersionedFile> files )
    {
        this.files = files;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String toString()
    {
        return name;
    }

    public boolean containsSandbox( File location )
    {
        for( Sandbox s : sandboxes )
        {
            if( s.getLocation().equals( location ) )
            {
                return true;
            }
        }

        return false;
    }
}
