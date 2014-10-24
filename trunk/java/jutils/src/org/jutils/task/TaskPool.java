package org.jutils.task;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

import org.jutils.concurrent.*;
import org.jutils.io.LogUtils;
import org.jutils.ui.event.ItemActionEvent;
import org.jutils.ui.event.ItemActionListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TaskPool
{
    /**  */
    private final IMultiTaskHandler tasker;
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
    public TaskPool( IMultiTaskHandler tasker, int numThreads )
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
        LogUtils.printDebug( "Shutting down" );
        this.pool.shutdown();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void startNext()
    {
        ITask task = null;

        synchronized( this )
        {
            if( tasker.canContinue() )
            {
                task = tasker.nextTask();
            }

            if( task != null )
            {
                ITaskView view = tasker.createView( task );

                TaskStopManager stopManager = new TaskStopManager();
                TaskRunner runner = new TaskRunner( task, view, stopManager );

                view.addCancelListener( new CancelListener( stopManager ) );
                stopManager.addFinishedListener( new FinishedListener( tasker,
                    view ) );

                pool.submit( runner );
            }
            else
            {
                pool.shutdown();
            }
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
            if( !pool.pool.isShutdown() )
            {
                pool.startNext();
            }

            int completedCount = ( int )pool.pool.getCompletedTaskCount() + 1;
            int taskCount = pool.tasker.getTaskCount();
            int percent = ( int )( completedCount * 100.0 / taskCount );
            String message = "Sets " + completedCount + " of " + taskCount +
                " completed";

            pool.tasker.signalMessage( message );
            pool.tasker.signalPercent( percent );
            LogUtils.printDebug( "Percent : " + completedCount );
        }

        @Override
        public void handleError( Throwable t )
        {
            pool.tasker.signalError( new TaskError(
                "An unrecoverable error occured", t ) );
            pool.shutdown();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class CancelListener implements ActionListener
    {
        private ITaskStopManager stopManager;

        public CancelListener( ITaskStopManager stopManager )
        {
            this.stopManager = stopManager;
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            stopManager.stop();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class FinishedListener implements
        ItemActionListener<Boolean>
    {
        private final IMultiTaskHandler tasker;
        private final ITaskView view;

        public FinishedListener( IMultiTaskHandler tasker, ITaskView view )
        {
            this.tasker = tasker;
            this.view = view;
        }

        @Override
        public void actionPerformed( ItemActionEvent<Boolean> event )
        {
            tasker.removeView( view );
        }
    }
}
