package org.cc.cvs.cmd;

/*******************************************************************************
 * Defines a generic method of updating status.
 ******************************************************************************/
public interface IStatusUpdater
{
    /***************************************************************************
     * Sets the percent complete to the given value.
     * @param percent the current percentage that the task has completed.
     **************************************************************************/
    public void updatePercent( double percent );

    public void updateStatus( String latestItem );
}
