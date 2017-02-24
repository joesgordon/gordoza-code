package org.jutils;

import java.util.Date;

/*******************************************************************************
 * Defines a class to time tasks.
 ******************************************************************************/
public class Stopwatch
{
    /** The start time in milliseconds since epoch. */
    private long startTime;
    /**  */
    private long elapsed;

    /**  */
    private WatchState state;

    /***************************************************************************
     * Creates a new, unhacked, stopwatch.
     **************************************************************************/
    public Stopwatch()
    {
        startTime = 0;

        start();
    }

    /***************************************************************************
     * Hacks the current system time to start the watch.
     * @return the current system time.
     **************************************************************************/
    public long start()
    {
        this.startTime = System.currentTimeMillis();
        this.elapsed = 0;
        this.state = WatchState.STARTED;

        return startTime;
    }

    /***************************************************************************
     * Hacks the current system time to stop the watch.
     * @return the current system time.
     **************************************************************************/
    public long stop()
    {
        long stopTime = System.currentTimeMillis();

        this.elapsed = stopTime - startTime;
        this.state = WatchState.STOPPED;

        return stopTime;
    }

    /***************************************************************************
     * Pauses the watch if started, resumes the watch if paused, and does
     * nothing if stopped.
     **************************************************************************/
    public void pauseResume()
    {
        long now = System.currentTimeMillis();
        long tempElapsed = now - startTime;

        switch( state )
        {
            case STARTED:
                elapsed += tempElapsed;
                startTime = 0;
                state = WatchState.PAUSED;
                break;
            case STOPPED:
                break;
            case PAUSED:
                startTime = now;
                state = WatchState.STARTED;
                break;
        }
    }

    /***************************************************************************
     * @return {@code true} if the watch is stopped; {@code false} otherwise.
     **************************************************************************/
    public boolean isStopped()
    {
        return state == WatchState.STOPPED;
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
     **************************************************************************/
    public long getElapsed()
    {
        long now = System.currentTimeMillis();
        long finalTime = elapsed;

        if( state == WatchState.STARTED )
        {
            finalTime += now - startTime;
        }

        return finalTime;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private enum WatchState
    {
        STOPPED,
        STARTED,
        PAUSED;
    }
}
