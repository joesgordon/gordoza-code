package chatterbox.io;

import java.io.IOException;

import org.jutils.io.IDataSerializer;
import org.jutils.io.IDataStream;

import chatterbox.data.ChatUser;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UserSerializer implements IDataSerializer<ChatUser>
{
    /**  */
    private final StringSerializer stringSerializer;

    /***************************************************************************
     * 
     **************************************************************************/
    public UserSerializer()
    {
        stringSerializer = new StringSerializer();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public ChatUser read( IDataStream stream ) throws IOException
    {
        String userId;
        String displayName;

        userId = stringSerializer.read( stream );
        displayName = stringSerializer.read( stream );

        return new ChatUser( userId, displayName );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( ChatUser user, IDataStream stream ) throws IOException
    {
        stringSerializer.write( user.userId, stream );
        stringSerializer.write( user.displayName, stream );
    }

}
