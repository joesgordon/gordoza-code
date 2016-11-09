package org.jutils.apps.summer.tasks;

import java.util.concurrent.atomic.AtomicInteger;

import org.jutils.apps.summer.data.ChecksumResult;
import org.jutils.task.IMultiTask;
import org.jutils.task.ITask;

/*******************************************************************************
 * 
 ******************************************************************************/
public class CreationTasksManager implements IMultiTask
{
    /**  */
    private final ChecksumResult input;
    /**  */
    private AtomicInteger index;

    /***************************************************************************
     * @param input
     **************************************************************************/
    public CreationTasksManager( ChecksumResult input )
    {
        this.input = input;

        this.index = new AtomicInteger( 0 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public ITask nextTask()
    {
        ITask task = null;
        int index = this.index.getAndIncrement();

        if( index < input.files.size() )
        {
            task = new CreationFileTask( input.files.get( index ), input.type );
        }

        return task;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public int getTaskCount()
    {
        return input.files.size();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getTaskAction()
    {
        return "Generating checksum for";
    }
}
