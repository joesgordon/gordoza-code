package chatterbox.io;

import java.io.*;

import org.jutils.io.IDataSerializer;

import chatterbox.data.ChatHeader;

public class ChatHeaderSerializer implements IDataSerializer<ChatHeader>
{
    public ChatHeaderSerializer()
    {
        ;
    }

    @Override
    public ChatHeader read( DataInput stream ) throws IOException
    {
        short type = stream.readShort();
        int length = stream.readInt();

        return new ChatHeader( type, length );
    }

    @Override
    public void write( ChatHeader header, DataOutput stream )
        throws IOException
    {
        stream.writeShort( header.getMessageType().toShort() );
        stream.writeInt( header.getMessageLength() );
    }
}
