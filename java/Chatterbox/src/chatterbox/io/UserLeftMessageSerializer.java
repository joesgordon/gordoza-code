package chatterbox.io;

import java.io.IOException;

import org.jutils.io.IDataSerializer;
import org.jutils.io.IDataStream;

import chatterbox.data.messages.UserLeftMessage;
import chatterbox.model.IUser;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UserLeftMessageSerializer
    implements IDataSerializer<UserLeftMessage>
{
    /**  */
    private StringSerializer stringSerializer;
    /**  */
    private UserSerializer userSerializer;

    /***************************************************************************
     * 
     **************************************************************************/
    public UserLeftMessageSerializer()
    {
        stringSerializer = new StringSerializer();
        userSerializer = new UserSerializer();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public UserLeftMessage read( IDataStream stream ) throws IOException
    {
        String conversationId;
        IUser user;

        conversationId = stringSerializer.read( stream );
        user = userSerializer.read( stream );

        return new UserLeftMessage( conversationId, user );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( UserLeftMessage message, IDataStream stream )
        throws IOException
    {
        stringSerializer.write( message.conversationId, stream );
        userSerializer.write( message.user, stream );
    }
}
