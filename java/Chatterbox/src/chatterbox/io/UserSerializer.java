package chatterbox.io;

import java.io.IOException;
import java.time.LocalDateTime;

import org.jutils.io.*;

import chatterbox.data.ChatUser;

/*******************************************************************************
 * Defines an {@link IDataSerializer} that reads/writes {@link ChatUser}s.
 ******************************************************************************/
public class UserSerializer implements IDataSerializer<ChatUser>
{
    /** For serializing {@link String}s. */
    private final StringSerializer stringSerializer;
    /** For serializing {@link LocalDateTime}s */
    private final LocalDateTimeSerializer ldtSerializer;

    /***************************************************************************
     * Creates a new user serializer.
     **************************************************************************/
    public UserSerializer()
    {
        this.stringSerializer = new StringSerializer();
        this.ldtSerializer = new LocalDateTimeSerializer();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public ChatUser read( IDataStream stream ) throws IOException
    {
        String userId;
        String displayName;

        userId = stringSerializer.read( stream );
        displayName = stringSerializer.read( stream );

        ChatUser user = new ChatUser( userId, displayName );

        user.available = stream.readBoolean();
        user.lastSeen = ldtSerializer.read( stream );

        return user;
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( ChatUser user, IDataStream stream ) throws IOException
    {
        stringSerializer.write( user.userId, stream );
        stringSerializer.write( user.displayName, stream );
        stream.writeBoolean( user.available );
        ldtSerializer.write( user.lastSeen, stream );
    }
}
