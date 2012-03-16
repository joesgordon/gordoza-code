package org.cc.data;

import java.util.ArrayList;
import java.util.List;

public class Release
{
    private String name;
    private String baseline;
    private List<ClosedTask> closedTasks;

    public Release()
    {
        closedTasks = new ArrayList<ClosedTask>();
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getBaseline()
    {
        return baseline;
    }

    public void setBaseline( String baseline )
    {
        this.baseline = baseline;
    }

    public List<ClosedTask> getClosedTasks()
    {
        return closedTasks;
    }

    public void setClosedTasks( List<ClosedTask> closedTasks )
    {
        this.closedTasks = new ArrayList<ClosedTask>( closedTasks );
    }

    @Override
    public String toString()
    {
        return name;
    }
}
