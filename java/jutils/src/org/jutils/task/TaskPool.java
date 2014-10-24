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
        this.pool = new SafeExecutorService( numThreads,
            new PoolFinishedHandler( this ) );
        this.currentTasks = new ArrayList<>( numThreads );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void start()
    {
        signalStatus( 0 );

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
                ITaskView view = tasker.createView( task.getName() );

                TaskStopManager stopManager = new TaskStopManager();
                ITaskHandler handler = new TaskHandlerWrapper( view,
                    stopManager, this );
                TaskRunner runner = new TaskRunner( task, handler );

                synchronized( currentTasks )
                {
                    currentTasks.add( runner );
                }

                view.addCancelListener( new CancelListener( stopManager ) );
                stopManager.addFinishedListener( new TaskFinishedListener(
                    this, runner, tasker, view ) );

                pool.submit( runner );
            }
            else
            {
                pool.shutdown();
            }
        }
    }

    /***************************************************************************
     * @param completedCount
     **************************************************************************/
    private void signalStatus( int completedCount )
    {
        int taskCount = tasker.getTaskCount();
        int percent = ( int )( completedCount * 100.0 / taskCount );
        String message = "Set " + completedCount;

        if( taskCount > -1 )
        {
            message += " of " + taskCount;
        }

        message += " completed";

        tasker.signalMessage( message );
        tasker.signalPercent( percent );

        // LogUtils.printDebug( "Percent : " + completedCount );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class PoolFinishedHandler implements IFinishedHandler
    {
        private TaskPool pool;

        public PoolFinishedHandler( TaskPool pool )
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

            pool.signalStatus( ( int )pool.pool.getCompletedTaskCount() + 1 );
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
    private static class TaskFinishedListener implements
        ItemActionListener<Boolean>
    {
        private final IMultiTaskHandler tasker;
        private final TaskRunner runner;
        private final ITaskView view;
        private final TaskPool pool;

        public TaskFinishedListener( TaskPool pool, TaskRunner runner,
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

    private static class TaskHandlerWrapper implements ITaskHandler
    {
        /**  */
        private final TaskHandler handler;
        private final TaskPool pool;

        public TaskHandlerWrapper( ITaskView view, TaskStopManager stopManager,
            TaskPool pool )
        {
            this.handler = new TaskHandler( view, stopManager );
            this.pool = pool;
        }

        @Override
        public boolean canContinue()
        {
            return handler.canContinue();
        }

        @Override
        public void signalMessage( String message )
        {
            handler.signalMessage( message );
        }

        @Override
        public void signalPercent( int percent )
        {
            handler.signalPercent( percent );
        }

        @Override
        public void signalError( TaskError error )
        {
            pool.tasker.signalError( error );
            pool.shutdown();
        }

        @Override
        public void signalFinished()
        {
            handler.signalFinished();
        }

        @Override
        public void stop()
        {
            handler.stop();
        }

        @Override
        public void stopAndWait() throws InterruptedException
        {
            handler.stopAndWait();
        }

        @Override
        public void addFinishedListener( ItemActionListener<Boolean> l )
        {
            handler.addFinishedListener( l );
        }
    }
}
