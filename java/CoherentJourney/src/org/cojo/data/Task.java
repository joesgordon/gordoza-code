package org.cojo.data;

import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Task
{
    /**  */
    public final int id;
    /**  */
    public final int authorId;

    /**  */
    public TaskState state;
    /**  */
    public String title;
    /**  */
    public String description;

    /**  */
    public int assigneeId;
    /**  */
    public TaskPriority priority;
    /**  */
    public int estimatedHours;
    /**  */
    public int actualHours;
    /**  */
    public String unitTestDescription;
    /**  */
    public String unitTestResults;

    /**  */
    public final List<Finding> codeReviews;

    /***************************************************************************
     * @param id
     * @param authorId
     **************************************************************************/
    public Task( int id, int authorId )
    {
        this.id = id;
        this.authorId = authorId;

        this.title = "";
        this.assigneeId = -1;
        this.estimatedHours = 0;
        this.actualHours = 0;

        this.description = "";
        this.unitTestDescription = "";
        this.unitTestResults = "";
        this.codeReviews = new ArrayList<>();
    }
}
