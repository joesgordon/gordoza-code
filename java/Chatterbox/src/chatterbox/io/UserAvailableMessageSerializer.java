package chatterbox.io;

import java.io.*;

import org.jutils.io.IDataSerializer;

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
    public UserAvailableMessage read( DataInput stream ) throws IOException
    {
        return new UserAvailableMessage( userSerializer.read( stream ) );
    }

    @Override
    public void write( UserAvailableMessage message, DataOutput stream )
        throws IOException
    {
        userSerializer.write( message.getUser(), stream );
    }

}
