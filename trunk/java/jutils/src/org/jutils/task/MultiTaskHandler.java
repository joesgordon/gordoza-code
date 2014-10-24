package org.jutils.task;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MultiTaskHandler implements IMultiTaskHandler
{
    /**  */
    private final IMultiTask tasker;
    /**  */
    private final IMultiTaskView view;

    /**  */
    public TaskError error;

    /***************************************************************************
     * @param tasker
     * @param view
     **************************************************************************/
    public MultiTaskHandler( IMultiTask tasker, IMultiTaskView view )
    {
        this.tasker = tasker;
        this.view = view;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public ITask nextTask()
    {
        return tasker.nextTask();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean canContinue()
    {
        return view.canContinue();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public ITaskView createView( String taskName )
    {
        String message = tasker.getTaskAction() + " " + taskName;
        ITaskView itv = view.addTaskView( message );

        return itv;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void signalError( TaskError error )
    {
        this.error = error;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void removeView( ITaskView view )
    {
        this.view.removeTask( view );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int getTaskCount()
    {
        return tasker.getTaskCount();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void signalPercent( int percent )
    {
        view.setPercent( percent );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void signalMessage( String message )
    {
        view.setTitle( message );
    }
}
