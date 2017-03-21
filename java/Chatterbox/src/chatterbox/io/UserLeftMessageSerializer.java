package chatterbox.io;

import java.io.IOException;

import org.jutils.io.IDataSerializer;
import org.jutils.io.IDataStream;

import chatterbox.data.ChatUser;
import chatterbox.data.messages.UserLeftMessage;

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
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public UserLeftMessage read( IDataStream stream ) throws IOException
    {
        String conversationId;
        ChatUser user;

        conversationId = stringSerializer.read( stream );
        user = userSerializer.read( stream );

        return new UserLeftMessage( conversationId, user );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( UserLeftMessage message, IDataStream stream )
        throws IOException
    {
        stringSerializer.write( message.conversationId, stream );
        userSerializer.write( message.user, stream );
    }
}
