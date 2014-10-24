package org.jutils.task;

public class MultiTaskHandler implements IMultiTaskHandler
{
    private final ITasker tasker;
    private final IMultiTaskView view;

    public MultiTaskHandler( ITasker tasker, IMultiTaskView view )
    {
        this.tasker = tasker;
        this.view = view;
    }

    @Override
    public ITask nextTask()
    {
        return tasker.nextTask();
    }

    @Override
    public boolean canContinue()
    {
        return view.canContinue();
    }

    @Override
    public ITaskView createView( ITask task )
    {
        String message = tasker.getTaskAction() + " " + task.getName();
        ITaskView itv = view.addTask( message );

        return itv;
    }

    @Override
    public void signalError( TaskError error )
    {
        view.signalError( error );
    }

    @Override
    public void removeView( ITaskView view )
    {
        this.view.removeTask( view );
    }

    @Override
    public int getTaskCount()
    {
        return tasker.getTaskCount();
    }

    @Override
    public void signalPercent( int percent )
    {
        view.setPercent( percent );
    }

    @Override
    public void signalMessage( String message )
    {
        view.setTitle( message );
    }
}
