package chatterbox.messenger;

import org.jutils.concurrent.TaskHandler;
import org.jutils.task.ITaskStatusHandler;
import org.jutils.task.TaskError;
import org.jutils.ui.event.ItemActionListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SignalerTaskHander implements ITaskStatusHandler
{
    /**  */
    private final ISignaler signaler;
    /**  */
    private final TaskHandler stopManager;

    /***************************************************************************
     * @param signaler
     **************************************************************************/
    public SignalerTaskHander( ISignaler signaler )
    {
        this.signaler = signaler;
        this.stopManager = new TaskHandler();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void signalMessage( String message )
    {
        signaler.signalMessage( message );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean signalPercent( int percent )
    {
        return signaler.signalPercent( percent );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void signalError( TaskError error )
    {
        signaler.signalError( error );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void signalFinished()
    {
        signaler.signalFinished();
        stopManager.signalFinished();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean canContinue()
    {
        return stopManager.canContinue();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void stop()
    {
        stopManager.stop();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean stopAndWaitFor()
    {
        return stopManager.stopAndWaitFor();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void addFinishedListener( ItemActionListener<Boolean> l )
    {
        stopManager.addFinishedListener( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void removeFinishedListener( ItemActionListener<Boolean> l )
    {
        stopManager.removeFinishedListener( l );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean isFinished()
    {
        return stopManager.isFinished();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean waitFor()
    {
        return stopManager.waitFor();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public boolean waitFor( long milliseconds )
    {
        return stopManager.waitFor( milliseconds );
    }
}
