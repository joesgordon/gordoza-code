package org.jutils.concurrent;

import org.jutils.Stopwatch;
import org.jutils.ui.event.ItemActionEvent;
import org.jutils.ui.event.ItemActionListener;

/*******************************************************************************
 * 
 ******************************************************************************/
public class Timeable extends Stoppable
{
    /**  */
    private final Stopwatch stopwatch;
    /**  */
    private final ItemActionListener<Long> finishedListener;

    /***************************************************************************
     * @param stoppable
     **************************************************************************/
    public Timeable( IStoppableTask timeable,
        ItemActionListener<Long> finishedListener )
    {
        super( timeable );

        this.finishedListener = finishedListener;
        this.stopwatch = new Stopwatch();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void run()
    {
        stopwatch.start();
        try
        {
            super.run();
        }
        finally
        {
            stopwatch.stop();

            ItemActionEvent<Long> event = new ItemActionEvent<Long>( this,
                stopwatch.getElapsed() );
            finishedListener.actionPerformed( event );
        }
    }
}
