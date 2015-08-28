package chatterbox.io;

import java.io.IOException;

import org.jutils.io.IDataSerializer;
import org.jutils.io.IDataStream;

import chatterbox.model.MessageAttributeSet;

public class MessageAttributeSetSerializer implements
    IDataSerializer<MessageAttributeSet>
{
    @Override
    public MessageAttributeSet read( IDataStream stream ) throws IOException
    {
        // TODO Serialize attribute set
        return null;
    }

    @Override
    public void write( MessageAttributeSet t, IDataStream stream )
        throws IOException
    {
        // TODO Serialize attribute set

    }
}
