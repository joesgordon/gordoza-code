package org.cojo.data;

import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChangeRequest
{
    /**  */
    public final int id;
    /**  */
    public final int authorId;

    /**  */
    public String title;
    /**  */
    public CrState state;
    /**  */
    public String description;

    /**  */
    public final List<SoftwareTask> tasks;
    /**  */
    public final List<Finding> reviews;

    /***************************************************************************
     * @param id
     * @param authorUserId
     **************************************************************************/
    public ChangeRequest( int id, int authorUserId )
    {
        this.id = id;
        this.authorId = authorUserId;
        this.title = "";
        this.description = "";
        this.state = CrState.NEW;
        this.tasks = new ArrayList<>();
        this.reviews = new ArrayList<>();
    }
}
