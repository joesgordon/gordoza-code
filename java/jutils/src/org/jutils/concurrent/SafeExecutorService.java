package org.jutils.concurrent;

import java.util.concurrent.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SafeExecutorService extends ThreadPoolExecutor
{
    /**  */
    private final IFinishedHandler finishedHandler;

    /***************************************************************************
     * @param numThreads
     * @param finishedHandler
     **************************************************************************/
    public SafeExecutorService( int numThreads,
        IFinishedHandler finishedHandler )
    {
        super( numThreads, // core threads
            numThreads, // max threads
            1, // timeout
            TimeUnit.MINUTES, // timeout units
            new LinkedBlockingQueue<Runnable>() // work queue
        );

        this.finishedHandler = finishedHandler;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    protected void afterExecute( Runnable r, Throwable t )
    {
        super.afterExecute( r, t );

        if( t == null && r instanceof Future<?> )
        {
            try
            {
                Future<?> future = ( Future<?> )r;
                if( future.isDone() )
                {
                    future.get();
                }

                finishedHandler.complete();
            }
            catch( CancellationException ce )
            {
                t = ce;
            }
            catch( ExecutionException ee )
            {
                t = ee.getCause();
            }
            catch( InterruptedException ie )
            {
                Thread.currentThread().interrupt(); // ignore/reset
            }
        }

        if( t != null )
        {
            finishedHandler.handleError( t );
        }
    }
}
