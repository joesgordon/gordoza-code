package org.jutils;

import java.util.Date;

/*******************************************************************************
 * Defines a class to time tasks.
 ******************************************************************************/
public class Stopwatch
{
    /** The start time in milliseconds since epoch. */
    private long startTime;
    /** The stop time in milliseconds since epoch. */
    private long stopTime;
    /** */
    private boolean stopped;

    /***************************************************************************
     * Creates a new, unhacked, stopwatch.
     **************************************************************************/
    public Stopwatch()
    {
        startTime = 0;
        stopTime = 0;
        stopped = false;

        start();
    }

    /***************************************************************************
     * Hacks the current system time to start the watch.
     * @return the current system time.
     **************************************************************************/
    public long start()
    {
        startTime = System.currentTimeMillis();
        stopTime = 0;

        return startTime;
    }

    /***************************************************************************
     * Hacks the current system time to stop the watch.
     * @return the currect system time.
     **************************************************************************/
    public long stop()
    {
        stopTime = System.currentTimeMillis();

        return stopTime;
    }

    /***************************************************************************
     * @return {@code true} if the watch is stopped; {@code false} otherwise.
     **************************************************************************/
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

    /***************************************************************************
     * @return the elapsed time in milliseconds
     * @throws IllegalStateException if the watch has not been stopped.
     **************************************************************************/
    public long getElapsed() throws IllegalStateException
    {
        if( stopTime == 0 )
        {
            throw new IllegalStateException(
                "The watch must first be stopped" );
        }

        return stopTime - startTime;
    }
}
