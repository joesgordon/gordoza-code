package org.jutils.task;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TaskMetrics
{
    /**  */
    public final long startTime;
    /**  */
    public final long stopTime;

    /***************************************************************************
     * @param start
     * @param stop
     **************************************************************************/
    public TaskMetrics( long start, long stop )
    {
        this.startTime = start;
        this.stopTime = stop;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public long getDuration()
    {
        return stopTime - startTime;
    }
}
