package org.jutils.task;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TaskUpdater
{
    /**  */
    public final ITaskHandler handler;
    /**  */
    public final Number length;

    /**  */
    private Number position;
    /**  */
    private int percent;

    /***************************************************************************
     * @param handler
     * @param length
     **************************************************************************/
    public TaskUpdater( ITaskHandler handler, Number length )
    {
        this.handler = handler;
        this.length = length;

        this.position = 0;
        this.percent = 0;

        handler.signalPercent( 0 );
    }

    /***************************************************************************
     * @param position
     **************************************************************************/
    public void update( Number position )
    {
        this.position = position;
        int percent = ( int )( position.doubleValue() * 100.0 /
            length.doubleValue() );
        if( percent != this.percent )
        {
            this.percent = percent;
            handler.signalPercent( percent );
        }
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public Number getPosition()
    {
        return position;
    }
}
