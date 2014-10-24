package org.jutils.task;

public class TaskMetrics
{
    public final long startTime;
    public final long stopTime;

    public TaskMetrics( long start, long stop )
    {
        this.startTime = start;
        this.stopTime = stop;
    }

    public long getDuration()
    {
        return stopTime - startTime;
    }
}
