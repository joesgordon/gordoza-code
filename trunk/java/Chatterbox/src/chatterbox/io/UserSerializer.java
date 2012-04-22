package chatterbox.io;

import java.io.IOException;

import org.jutils.io.IDataSerializer;
import org.jutils.io.IDataStream;

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
    public IUser read( IDataStream stream ) throws IOException
    {
        String userId;
        String displayName;

        userId = stringSerializer.read( stream );
        displayName = stringSerializer.read( stream );

        return new ChatUser( userId, displayName );
    }

    @Override
    public void write( IUser user, IDataStream stream ) throws IOException
    {
        stringSerializer.write( user.getUserId(), stream );
        stringSerializer.write( user.getDisplayName(), stream );
    }

}
