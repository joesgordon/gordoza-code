package org.jutils;

import java.util.Date;

import org.jutils.concurrent.IStopwatch;

public class Stopwatch implements IStopwatch
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

    @Override
    public void start()
    {
        startTime = System.currentTimeMillis();
        stopTime = 0;
    }

    @Override
    public void stop()
    {
        stopTime = System.currentTimeMillis();
    }

    @Override
    public boolean isStopped()
    {
        return stopped;
    }

    /***************************************************************************
     * Returns the elapsed time in a <code>Date</code> object.
     * @return the elapsed time in a <code>Date</code> object.
     **************************************************************************/
    @Override
    public Date getElapsedDate()
    {
        return new Date( getElapsed() );
    }

    @Override
    public long getElapsed()
    {
        if( stopTime == 0 )
        {
            throw new IllegalStateException( "The watch must first be stopped" );
        }

        return stopTime - startTime;
    }
}
