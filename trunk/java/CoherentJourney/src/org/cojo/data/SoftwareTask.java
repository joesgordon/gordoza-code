package org.cojo.data;

import java.util.List;

import org.cojo.model.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SoftwareTask implements ISoftwareTask
{
    private int number;
    private String title;
    private IUser lead;
    private int estimatedHours;
    private int actualHours;
    private boolean codeReviewRequired;
    private String description;
    private String unitTestDescription;
    private String unitTestResults;
    private List<IFinding> codeReviews;

    /***************************************************************************
     * 
     **************************************************************************/
    public SoftwareTask( int number, String title, IUser lead, int estHours,
        int actHours, boolean codeRevRequired, String desc, String utDesc,
        String utResults, List<IFinding> codeReviews )
    {
        this.number = number;
        this.title = title;
        this.lead = lead;
        this.estimatedHours = estHours;
        this.actualHours = actHours;
        this.codeReviewRequired = codeRevRequired;

        this.description = desc;
        this.unitTestDescription = utDesc;
        this.unitTestResults = utResults;
        this.codeReviews = codeReviews;
    }

    @Override
    public int getActualHours()
    {
        return actualHours;
    }

    @Override
    public int getEstimatedHours()
    {
        return estimatedHours;
    }

    @Override
    public IUser getLead()
    {
        return lead;
    }

    @Override
    public int getNumber()
    {
        return number;
    }

    @Override
    public String getTitle()
    {
        return title;
    }

    @Override
    public boolean isCodeReviewRequired()
    {
        return codeReviewRequired;
    }

    @Override
    public String getDescription()
    {
        return description;
    }

    @Override
    public String getUnitTestDescription()
    {
        return unitTestDescription;
    }

    @Override
    public String getUnitTestResults()
    {
        return unitTestResults;
    }

    @Override
    public List<IFinding> getCodeReviews()
    {
        return codeReviews;
    }
}
