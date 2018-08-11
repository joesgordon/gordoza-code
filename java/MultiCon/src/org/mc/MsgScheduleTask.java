package org.mc;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.jutils.net.IConnection;

/*******************************************************************************
 * 
 ******************************************************************************/
public class MsgScheduleTask
{
    /**  */
    private final Timer taskTimer;
    /**  */
    private final byte[] msg;
    /**  */
    private final IConnection connection;

    /**  */
    private double calcRate;
    /**  */
    private long msgsSent;

    /***************************************************************************
     * @param rate
     * @param msg
     * @param connection
     **************************************************************************/
    public MsgScheduleTask( double rate, byte[] msg, IConnection connection )
    {
        this.taskTimer = new Timer( "Message Sender @ " + rate + " Hz" );
        this.msg = msg;
        this.connection = connection;
        this.msgsSent = 0L;

        long delay = ( long )Math.floor( 1000.0 / rate );

        taskTimer.schedule( new TimerTaskRunner( () -> run() ), 0, delay );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void run()
    {
        try
        {
            connection.sendMessage( msg );
        }
        catch( IOException ex )
        {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void stop()
    {
        taskTimer.cancel();
    }

    /**
     *
     */
    private static class TimerTaskRunner extends TimerTask
    {
        /**  */
        private Runnable runner;

        /**
         * @param runner
         */
        public TimerTaskRunner( Runnable runner )
        {
            this.runner = runner;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void run()
        {
            runner.run();
        }
    }
}
