package org.mc;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.jutils.net.IConnection;
import org.jutils.net.NetMessage;
import org.jutils.ui.event.updater.IUpdater;

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
    private final IUpdater<NetMessage> txCallback;

    /**  */
    private long msgsSent;

    /***************************************************************************
     * @param rate
     * @param msg
     * @param connection
     **************************************************************************/
    public MsgScheduleTask( double rate, byte[] msg, IConnection connection,
        IUpdater<NetMessage> txCallback )
    {
        this.taskTimer = new Timer( "Message Sender @ " + rate + " Hz" );
        this.msg = msg;
        this.connection = connection;
        this.txCallback = txCallback;

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
            NetMessage netMsg = connection.sendMessage( msg );

            msgsSent++;

            txCallback.update( netMsg );
        }
        catch( IOException ex )
        {
            stop();

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
