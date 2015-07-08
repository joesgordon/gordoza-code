package chatterbox.messenger;

import java.util.Timer;
import java.util.TimerTask;

import chatterbox.model.IUser;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UserCheckTask
{
    /**  */
    private ChatRoom chat;
    /**  */
    private Timer timer;
    /**  */
    private IUser user;
    /**  */
    private TimerTask unAvailableTask;
    /**  */
    private TimerTask leftTask;

    /***************************************************************************
     * @param chat
     * @param user
     **************************************************************************/
    public UserCheckTask( ChatRoom chat, IUser user )
    {
        this.chat = chat;
        this.user = user;

        reset();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void cancel()
    {
        timer.cancel();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void advance()
    {
        unAvailableTask.cancel();
        timer.schedule( leftTask, 20000 );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void initTasks()
    {
        unAvailableTask = new TimerTask()
        {
            @Override
            public void run()
            {
                chat.setUserAvailable( user, false );
            }
        };

        leftTask = new TimerTask()
        {
            @Override
            public void run()
            {
                chat.removeUser( user );
                chat = null;
                user = null;
                timer = null;
                unAvailableTask = null;
                leftTask = null;
            }
        };
    }

    /***************************************************************************
     * 
     **************************************************************************/
    public void reset()
    {
        if( unAvailableTask != null )
        {
            unAvailableTask.cancel();
            leftTask.cancel();
            timer.purge();
        }

        initTasks();

        timer = new Timer( user.getUserId() + "'s Timer" );
        timer.schedule( unAvailableTask, 8000 );
    }
}
