package org.taskflow.data;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 *
 ******************************************************************************/
public class Project
{
    /**  */
    public String title;
    /**  */
    public String description;
    /**  */
    public List<Task> tasks;
    /**  */
    public LocalDateTime dateCreated;
    /**  */
    public LocalDateTime dateUpdated;
    /**  */
    private transient File file;

    /***************************************************************************
     *
     **************************************************************************/
    public Project()
    {
        this.title = "";
        this.description = "";
        this.tasks = new ArrayList<>();
        this.dateCreated = LocalDateTime.now();
        this.dateUpdated = dateCreated;
        this.file = null;
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
