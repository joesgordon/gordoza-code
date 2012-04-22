package chatterbox.io;

import java.io.IOException;

import org.jutils.io.IDataSerializer;
import org.jutils.io.IDataStream;

import chatterbox.model.IMessageAttributeSet;

public class MessageAttributeSetSerializer implements
    IDataSerializer<IMessageAttributeSet>
{
    @Override
    public IMessageAttributeSet read( IDataStream stream ) throws IOException
    {
        // TODO Serialize attribute set
        return null;
    }

    @Override
    public void write( IMessageAttributeSet t, IDataStream stream )
        throws IOException
    {
        // TODO Serialize attribute set

    }
}
