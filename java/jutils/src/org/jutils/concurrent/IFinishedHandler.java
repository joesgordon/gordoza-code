package org.jutils.concurrent;

public interface IFinishedHandler
{
    public void complete();

    public void handleError( Throwable t );
}
