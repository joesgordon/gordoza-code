package org.jutils;

import java.util.Date;

public class Stopwatch
{
    /** The start time in millis since epoch. */
    private long startTime;
    /** The stop time in millis since epoch. */
    private long stopTime;
    /** */
    private boolean stopped;

    public Stopwatch()
    {
        startTime = 0;
        stopTime = 0;
        stopped = false;

        start();
    }

    public long start()
    {
        startTime = System.currentTimeMillis();
        stopTime = 0;

        return startTime;
    }

    public long stop()
    {
        stopTime = System.currentTimeMillis();

        return stopTime;
    }

    public boolean isStopped()
    {
        return stopped;
    }

    /***************************************************************************
     * Returns the elapsed time in a <code>Date</code> object.
     * @return the elapsed time in a <code>Date</code> object.
     **************************************************************************/
    public Date getElapsedDate()
    {
        return new Date( getElapsed() );
    }

    public long getElapsed()
    {
        if( stopTime == 0 )
        {
            throw new IllegalStateException(
                "The watch must first be stopped" );
        }

        return stopTime - startTime;
    }
}
