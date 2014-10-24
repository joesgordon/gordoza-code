package org.jutils.concurrent;

public interface IFinishedHandler
{
    public void handleError( Throwable t );

    public void complete();
}
