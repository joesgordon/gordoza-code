package org.cojo.data;

import java.util.List;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SoftwareTask
{
    /**  */
    public int id;
    /**  */
    public String title;
    /**  */
    public int leadUserId;
    /**  */
    public int estimatedHours;
    /**  */
    public int actualHours;
    /**  */
    public boolean codeReviewRequired;
    /**  */
    public String description;
    /**  */
    public String unitTestDescription;
    /**  */
    public String unitTestResults;
    /**  */
    public List<Finding> codeReviews;

    /***************************************************************************
     * 
     **************************************************************************/
    public SoftwareTask( int id, String title, int leadUserId, int estHours,
        int actHours, boolean codeRevRequired, String desc, String utDesc,
        String utResults, List<Finding> codeReviews )
    {
        this.id = id;
        this.title = title;
        this.leadUserId = leadUserId;
        this.estimatedHours = estHours;
        this.actualHours = actHours;
        this.codeReviewRequired = codeRevRequired;

        this.description = desc;
        this.unitTestDescription = utDesc;
        this.unitTestResults = utResults;
        this.codeReviews = codeReviews;
    }
}
