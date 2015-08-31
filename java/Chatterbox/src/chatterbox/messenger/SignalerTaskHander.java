package chatterbox.messenger;

import org.jutils.concurrent.TaskStopManager;
import org.jutils.task.ITaskHandler;
import org.jutils.task.TaskError;
import org.jutils.ui.event.ItemActionListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class SignalerTaskHander implements ITaskHandler
{
    /**  */
    private final ISignaler signaler;
    /**  */
    private final TaskStopManager stopManager;

    /***************************************************************************
     * @param signaler
     **************************************************************************/
    public SignalerTaskHander( ISignaler signaler )
    {
        this.signaler = signaler;
        this.stopManager = new TaskStopManager();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void signalMessage( String message )
    {
        signaler.signalMessage( message );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void signalPercent( int percent )
    {
        signaler.signalPercent( percent );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void signalError( TaskError error )
    {
        signaler.signalError( error );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void signalFinished()
    {
        signaler.signalFinished();
        stopManager.signalFinished();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public boolean canContinue()
    {
        return stopManager.continueProcessing();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void stop()
    {
        stopManager.stop();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void stopAndWait() throws InterruptedException
    {
        stopManager.stopAndWait();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void addFinishedListener( ItemActionListener<Boolean> l )
    {
        stopManager.addFinishedListener( l );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void removeFinishedListener( ItemActionListener<Boolean> l )
    {
        stopManager.removeFinishedListener( l );
    }
}
