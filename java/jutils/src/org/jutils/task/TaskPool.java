package org.jutils.task;

import java.util.concurrent.TimeUnit;

import org.jutils.concurrent.IFinishedHandler;
import org.jutils.concurrent.SafeExecutorService;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TaskPool
{
    /**  */
    private final ITasker tasker;
    /**  */
    private final int numThreads;
    /**  */
    private final SafeExecutorService pool;

    /***************************************************************************
     * @param tasker
     * @param numThreads
     * @param stopManager
     * @param view
     **************************************************************************/
    public TaskPool( ITasker tasker, int numThreads )
    {
        this.tasker = tasker;
        this.numThreads = numThreads;
        this.pool = new SafeExecutorService( numThreads, new FinishedHandler(
            this ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void start()
    {
        for( int i = 0; i < numThreads; i++ )
        {
            startNext();
        }

        while( tasker.canContinue() )
        {
            try
            {
                pool.awaitTermination( 250, TimeUnit.MILLISECONDS );

                if( pool.isTerminated() )
                {
                    break;
                }
            }
            catch( InterruptedException e )
            {
                break;
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void shutdown()
    {
        this.pool.shutdown();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void startNext()
    {
        ITask task = null;

        if( tasker.canContinue() )
        {
            task = tasker.nextTask();
        }

        if( task != null )
        {
            ITaskView view = tasker.createView();

            TaskRunner runner = new TaskRunner( task, view );

            pool.submit( runner );
        }
        else
        {
            pool.shutdown();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FinishedHandler implements IFinishedHandler
    {
        private TaskPool pool;

        public FinishedHandler( TaskPool pool )
        {
            this.pool = pool;
        }

        @Override
        public void complete()
        {
            pool.startNext();
        }

        @Override
        public void handleError( Throwable t )
        {
            pool.tasker.signalError( new TaskError(
                "An unrecoverable error occured", t ) );
            pool.shutdown();
        }
    }
}
