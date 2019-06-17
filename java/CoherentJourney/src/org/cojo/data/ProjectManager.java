package org.cojo.data;

import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ProjectManager
{
    /**  */
    private int currentUserId;
    /**  */
    public Project project;

    /***************************************************************************
     * 
     **************************************************************************/
    public ProjectManager()
    {
        this.currentUserId = ProjectUser.INVALID_ID;
        this.project = new Project();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public ProjectUser getUser()
    {
        return project.getUser( currentUserId );
    }

    /***************************************************************************
     * @param firstName
     * @param lastName
     * @return
     **************************************************************************/
    public ProjectUser createUser( String firstName, String lastName )
    {
        ProjectUser u = new ProjectUser( project.users.size() );

        u.user.firstName = firstName;
        u.user.lastName = lastName;

        return u;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Task createTask()
    {
        Task task = new Task( nextTaskId(), getUser().id );

        return task;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    private int nextTaskId()
    {
        int id = -1;

        for( Task task : project.tasks )
        {
            id = Math.max( id, task.id );
        }

        return id + 1;
    }

    /***************************************************************************
     * @param title
     * @return
     **************************************************************************/
    public List<Task> findTasks( String title )
    {
        List<Task> tasks = new ArrayList<>();
        // TODO Auto-generated method stub
        return tasks;
    }
}
