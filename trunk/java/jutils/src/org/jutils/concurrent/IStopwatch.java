package org.jutils.concurrent;

import java.util.Date;

public interface IStopwatch
{
    public void start();

    public void stop();

    public boolean isStopped();

    /**
     * Returns the number of milliseconds elapsed since start.
     * @return
     * @throws IllegalStateException
     */
    public long getElapsed();

    public Date getElapsedDate();
}
