package org.jutils.task;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jutils.concurrent.*;
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

    /**  */
    private final List<TaskRunner> currentTasks;

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
        this.currentTasks = new ArrayList<>( numThreads );
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
        // LogUtils.printDebug( "Shutting down" );
        this.pool.shutdown();

        synchronized( currentTasks )
        {
            for( TaskRunner r : currentTasks )
            {
                r.stop();
            }
        }
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

                synchronized( currentTasks )
                {
                    currentTasks.add( runner );
                }

                view.addCancelListener( new CancelListener( stopManager ) );
                stopManager.addFinishedListener( new FinishedListener( this,
                    runner, tasker, view ) );

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
            // LogUtils.printDebug( "Percent : " + completedCount );
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
        private final TaskRunner runner;
        private final ITaskView view;
        private final TaskPool pool;

        public FinishedListener( TaskPool pool, TaskRunner runner,
            IMultiTaskHandler tasker, ITaskView view )
        {
            this.pool = pool;
            this.runner = runner;
            this.tasker = tasker;
            this.view = view;
        }

        @Override
        public void actionPerformed( ItemActionEvent<Boolean> event )
        {
            synchronized( pool.currentTasks )
            {
                pool.currentTasks.remove( runner );
            }

            tasker.removeView( view );
        }
    }
}
