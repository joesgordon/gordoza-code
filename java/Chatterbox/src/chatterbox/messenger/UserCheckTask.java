package chatterbox.messenger;

import java.awt.MouseInfo;
import java.awt.Point;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.jutils.ValidationException;
import org.jutils.task.*;

import chatterbox.data.ChatUser;
import chatterbox.data.messages.UserAvailableMessage;

/*******************************************************************************
 * Defines a task that <ol><li>updates the local user's
 * {@link ChatUser#lastSeen} time and {@link ChatUser#available} status,
 * <li>sends the user's availability periodically, and <li>maintains the list of
 * active users.</ol>
 ******************************************************************************/
public class UserCheckTask implements IStatusTask
{
    /** The period of time between checks in milliseconds. Run at 1 Hz. */
    private static final long CHECK_PERIOD = 1000;
    /**
     * The number of seconds a user can be idle before they are marked as away.
     */
    private static final long AWAY_LIMIT = 5 * 60;
    /**
     * The number of seconds a user can be idle before they are removed from the
     * list of available users.
     */
    private static final long AVAILABILITY_LIMIT = 10 * 60;

    /**  */
    private final ChatterboxHandler chat;
    /**  */
    private final List<UserData> userlogs;

    /**  */
    private Point lastMouseCursor;

    /***************************************************************************
     * @param chat
     * @param user
     **************************************************************************/
    public UserCheckTask( ChatterboxHandler chat )
    {
        this.chat = chat;
        this.userlogs = new ArrayList<>();
    }

    // TODO add and call listeners saying the user status has changed.

    /***************************************************************************
     * @param user
     **************************************************************************/
    public void markSeen( ChatUser user )
    {
        synchronized( userlogs )
        {
            boolean addUser = true;
            for( UserData ud : userlogs )
            {
                if( ud.user.equals( user ) )
                {
                    addUser = false;
                    ud.lastAvailable = LocalDateTime.now();
                    break;
                }
            }

            if( addUser )
            {
                userlogs.add( new UserData( user ) );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getName()
    {
        return "Chatterbox User Check Task";
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void run( ITaskStatusHandler handler )
    {
        while( handler.canContinue() )
        {
            updateUserStatus();

            sendAvailable( handler );

            updateUserLogs();

            try
            {
                Thread.sleep( CHECK_PERIOD );
            }
            catch( InterruptedException e )
            {
                break;
            }
        }

    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void updateUserStatus()
    {
        Point cursorLoc = MouseInfo.getPointerInfo().getLocation();
        LocalDateTime now = LocalDateTime.now();
        ChatUser user = chat.getLocalUser();

        if( !cursorLoc.equals( lastMouseCursor ) )
        {
            user.lastSeen = now;
            user.away = false;
            lastMouseCursor = cursorLoc;
        }
        else if( ChronoUnit.SECONDS.between( user.lastSeen, now ) > AWAY_LIMIT )
        {
            user.away = true;
        }
    }

    /***************************************************************************
     * @param handler
     **************************************************************************/
    private void sendAvailable( ITaskStatusHandler handler )
    {
        ChatUser user = chat.getLocalUser();
        UserAvailableMessage message = new UserAvailableMessage( user );

        try
        {
            chat.sendMessage( message );
        }
        catch( IOException ex )
        {
            handler.signalError( new TaskError( "Send I/O Error", ex ) );
        }
        catch( ValidationException ex )
        {
            handler.signalError( new TaskError( "Send Validation Error", ex ) );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private void updateUserLogs()
    {
        List<UserData> toRemove = new ArrayList<>();
        synchronized( userlogs )
        {
            LocalDateTime now = LocalDateTime.now();

            for( UserData ud : userlogs )
            {
                if( ud.isAway( now ) )
                {
                    ud.user.away = true;
                }
                else if( ud.isUnavailable( now ) )
                {
                    toRemove.add( ud );
                }
            }

            if( !toRemove.isEmpty() )
            {
                userlogs.removeAll( toRemove );
            }
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static final class UserData
    {
        public final ChatUser user;
        public LocalDateTime lastAvailable;

        public UserData( ChatUser user )
        {
            this.user = user;
            this.lastAvailable = LocalDateTime.now();
        }

        public boolean isAway( LocalDateTime now )
        {
            long delta = ChronoUnit.SECONDS.between( lastAvailable, now );

            return delta > AWAY_LIMIT;
        }

        public boolean isUnavailable( LocalDateTime now )
        {
            long delta = ChronoUnit.SECONDS.between( lastAvailable, now );

            return delta > AVAILABILITY_LIMIT;
        }
    }
}
