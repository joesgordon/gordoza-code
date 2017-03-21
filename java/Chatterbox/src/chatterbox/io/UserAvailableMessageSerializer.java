package chatterbox.io;

import java.io.IOException;

import org.jutils.io.IDataSerializer;
import org.jutils.io.IDataStream;

import chatterbox.data.ChatUser;
import chatterbox.data.messages.UserAvailableMessage;

/*******************************************************************************
 * Defines an {@link IDataSerializer} that reads/writes
 * {@link UserAvailableMessage}s.
 ******************************************************************************/
public class UserAvailableMessageSerializer
    implements IDataSerializer<UserAvailableMessage>
{
    /** For serializing {@link ChatUser}s. */
    private final UserSerializer userSerializer;

    /***************************************************************************
     * Creates a new user available message serializer.
     **************************************************************************/
    public UserAvailableMessageSerializer()
    {
        this.userSerializer = new UserSerializer();
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public UserAvailableMessage read( IDataStream stream ) throws IOException
    {
        return new UserAvailableMessage( userSerializer.read( stream ) );
    }

    /***************************************************************************
     * {@inheritDoc}
     **************************************************************************/
    @Override
    public void write( UserAvailableMessage message, IDataStream stream )
        throws IOException
    {
        userSerializer.write( message.user, stream );
    }
}
