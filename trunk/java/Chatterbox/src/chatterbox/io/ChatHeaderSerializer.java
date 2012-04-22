package chatterbox.io;

import java.io.IOException;

import org.jutils.io.IDataSerializer;
import org.jutils.io.IDataStream;

import chatterbox.data.ChatHeader;

public class ChatHeaderSerializer implements IDataSerializer<ChatHeader>
{
    public ChatHeaderSerializer()
    {
        ;
    }

    @Override
    public ChatHeader read( IDataStream stream ) throws IOException
    {
        short type = stream.readShort();
        int length = stream.readInt();

        return new ChatHeader( type, length );
    }

    @Override
    public void write( ChatHeader header, IDataStream stream )
        throws IOException
    {
        stream.writeShort( header.getMessageType().toShort() );
        stream.writeInt( header.getMessageLength() );
    }
}
