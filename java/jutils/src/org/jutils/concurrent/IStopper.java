package org.jutils.concurrent;

import org.jutils.ui.event.IEventListener;

public interface IStopper
{
    public void stop();

    public void addStoppedListener( IEventListener l );

    public boolean isFinished();

    public void waitFor() throws InterruptedException;

    public void stopAndWaitFor() throws InterruptedException;

    public boolean continueProcessing();

    public void signalFinished();
}
