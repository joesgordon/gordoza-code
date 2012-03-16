package chatterbox.io;

import java.io.*;

import org.jutils.io.IDataSerializer;

import chatterbox.model.IMessageAttributeSet;

public class MessageAttributeSetSerializer implements
    IDataSerializer<IMessageAttributeSet>
{
    @Override
    public IMessageAttributeSet read( DataInput stream ) throws IOException
    {
        // TODO Serialize attribute set
        return null;
    }

    @Override
    public void write( IMessageAttributeSet t, DataOutput stream )
        throws IOException
    {
        // TODO Serialize attribute set

    }
}
