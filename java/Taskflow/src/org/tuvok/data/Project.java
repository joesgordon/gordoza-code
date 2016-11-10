package org.tuvok.data;

import java.io.File;
import java.util.*;

/*******************************************************************************
 *
 ******************************************************************************/
public class Project
{
    /**  */
    public String title = "";
    /**  */
    public String description = "";
    /**  */
    public List<Task> tasks = new ArrayList<Task>();
    /**  */
    public GregorianCalendar dateCreated = new GregorianCalendar();
    /**  */
    public GregorianCalendar dateUpdated = new GregorianCalendar();
    /**  */
    private transient File file = null;

    /***************************************************************************
     *
     **************************************************************************/
    public Project()
    {
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String toString()
    {
        return title;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean equals( Object obj )
    {
        boolean equal = false;
        try
        {
            Project otherProject = ( Project )obj;
            if( otherProject.file == null || this.file == null )
            {
                equal = otherProject == this;
            }
            else
            {
                equal = otherProject.file.equals( this.file );
            }
        }
        catch( ClassCastException ex )
        {
            equal = false;
        }
        return equal;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int hashCode()
    {
        return file.hashCode();
    }
}
