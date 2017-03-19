package chatterbox.messenger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jutils.ValidationException;
import org.jutils.task.*;

import chatterbox.ChatterboxConstants;
import chatterbox.data.ChatUser;
import chatterbox.data.messages.UserAvailableMessage;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UserCheckTask implements ITask
{
    /**  */
    private final ChatterboxHandler chat;
    /**  */
    private final List<UserData> userlogs;

    /***************************************************************************
     * @param chat
     * @param user
     **************************************************************************/
    public UserCheckTask( ChatterboxHandler chat )
    {
        this.chat = chat;
        this.userlogs = new ArrayList<>();
    }

    /***************************************************************************
     * @param user
     **************************************************************************/
    public void markSeen( ChatUser user )
    {
        synchronized( userlogs )
        {
            for( UserData ud : userlogs )
            {
                if( ud.user.equals( user ) )
                {
                    ud.lastSeen = ChatterboxConstants.now();
                    break;
                }
            }

            userlogs.add( new UserData( user ) );
        }
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public String getName()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void run( ITaskHandler handler )
    {
        List<UserData> toRemove = new ArrayList<>();

        while( handler.canContinue() )
        {
            try
            {
                sendAvailable();
            }
            catch( IOException ex )
            {
                handler.signalError( new TaskError( "Send I/O Error", ex ) );
            }
            catch( ValidationException ex )
            {
                handler.signalError(
                    new TaskError( "Send Validation Error", ex ) );
            }

            synchronized( userlogs )
            {
                long now = ChatterboxConstants.now();

                for( UserData ud : userlogs )
                {
                    long delta = now - ud.lastSeen;

                    if( delta > 60000 )
                    {
                        chat.removeUser( ud.user );
                        toRemove.add( ud );
                    }
                }

                if( !toRemove.isEmpty() )
                {
                    userlogs.removeAll( toRemove );
                    toRemove.clear();
                }
            }

            try
            {
                Thread.sleep( 1000 );
            }
            catch( InterruptedException e )
            {
                break;
            }
        }
    }

    /***************************************************************************
     * @throws IOException
     * @throws ValidationException
     **************************************************************************/
    private void sendAvailable() throws IOException, ValidationException
    {
        ChatUser user = chat.getLocalUser();
        UserAvailableMessage message = new UserAvailableMessage( user );

        chat.sendMessage( message );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class UserData
    {
        public ChatUser user;
        public long lastSeen;

        public UserData( ChatUser user )
        {
            this.user = user;
            this.lastSeen = ChatterboxConstants.now();
        }
    }
}
