package chatterbox.io;

import java.io.IOException;

import org.jutils.io.IDataSerializer;
import org.jutils.io.IDataStream;

import chatterbox.data.messages.UserAvailableMessage;

public class UserAvailableMessageSerializer implements
    IDataSerializer<UserAvailableMessage>
{
    private UserSerializer userSerializer;

    public UserAvailableMessageSerializer()
    {
        userSerializer = new UserSerializer();
    }

    @Override
    public UserAvailableMessage read( IDataStream stream ) throws IOException
    {
        return new UserAvailableMessage( userSerializer.read( stream ) );
    }

    @Override
    public void write( UserAvailableMessage message, IDataStream stream )
        throws IOException
    {
        userSerializer.write( message.getUser(), stream );
    }

}
