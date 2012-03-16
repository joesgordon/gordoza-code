package chatterbox.io;

import java.io.*;

import org.jutils.io.IDataSerializer;

import chatterbox.data.messages.UserLeftMessage;
import chatterbox.model.IUser;

/*******************************************************************************
 * 
 ******************************************************************************/
public class UserLeftMessageSerializer implements
    IDataSerializer<UserLeftMessage>
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
    public UserLeftMessage read( DataInput stream ) throws IOException
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
    public void write( UserLeftMessage message, DataOutput stream )
        throws IOException
    {
        stringSerializer.write( message.getConversationId(), stream );
        userSerializer.write( message.getUser(), stream );
    }
}
