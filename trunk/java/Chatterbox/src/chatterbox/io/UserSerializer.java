package chatterbox.io;

import java.io.*;

import org.jutils.io.IDataSerializer;

import chatterbox.data.ChatUser;
import chatterbox.model.IUser;

public class UserSerializer implements IDataSerializer<IUser>
{
    private StringSerializer stringSerializer;

    public UserSerializer()
    {
        stringSerializer = new StringSerializer();
    }

    @Override
    public IUser read( DataInput stream ) throws IOException
    {
        String userId;
        String displayName;

        userId = stringSerializer.read( stream );
        displayName = stringSerializer.read( stream );

        return new ChatUser( userId, displayName );
    }

    @Override
    public void write( IUser user, DataOutput stream ) throws IOException
    {
        stringSerializer.write( user.getUserId(), stream );
        stringSerializer.write( user.getDisplayName(), stream );
    }

}
