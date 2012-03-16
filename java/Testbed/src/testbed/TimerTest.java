package testbed;

import java.util.Timer;
import java.util.TimerTask;

public class TimerTest
{
    public static void main( String[] args )
    {
        Timer bulletTimer = new Timer();
        bulletTimer.schedule( new BulletTimerTask( bulletTimer ), 0, 2 );
    }

    private static class BulletTimerTask extends TimerTask
    {
        private final Timer timer;
        private int count;
        private long diffSums;
        private long lastTime;

        public BulletTimerTask( Timer t )
        {
            diffSums = 0;
            count = 0;
            lastTime = 0;
            timer = t;
        }

        @Override
        public void run()
        {
            long thisTime = System.currentTimeMillis();

            count++;

            if( count > 1 )
            {
                diffSums += ( thisTime - lastTime );
            }

            lastTime = thisTime;

            if( count >= 10000 )
            {
                double avg = diffSums / count;
                System.out.println( "" + count );
                System.out.println( "Average time diff was " +
                    String.format( "%5f", avg ) + " ms" );
                timer.cancel();
            }
        }
    }
}
