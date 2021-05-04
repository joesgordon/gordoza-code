package chatterbox.io;

import java.io.IOException;

import org.jutils.core.ValidationException;
import org.jutils.core.io.IDataSerializer;
import org.jutils.core.io.IDataStream;

import chatterbox.data.messages.ChatHeader;
import chatterbox.data.messages.ChatMessageType;

public class ChatHeaderSerializer implements IDataSerializer<ChatHeader>
{
    private static final long HEADER_MAGiC_NUM = 0x20FE90FA286BEB40L;

    @Override
    public ChatHeader read( IDataStream stream )
        throws IOException, ValidationException
    {
        if( stream.getAvailable() < 14 )
        {
            throw new ValidationException(
                "Message length too short: " + stream.getAvailable() );
        }

        long num;
        short msgTypeNum;
        ChatMessageType messageType;
        int length;

        num = stream.readLong();

        if( num != HEADER_MAGiC_NUM )
        {
            throw new ValidationException(
                "A non-Chatterbox message was received." );
        }

        msgTypeNum = stream.readShort();
        try
        {
            messageType = ChatMessageType.fromShort( msgTypeNum );
        }
        catch( IllegalArgumentException ex )
        {
            throw new ValidationException(
                "Invalid message type: " + msgTypeNum, ex );
        }
        length = stream.readInt();

        return new ChatHeader( messageType, length );
    }

    @Override
    public void write( ChatHeader header, IDataStream stream )
        throws IOException
    {
        stream.writeLong( HEADER_MAGiC_NUM );
        stream.writeShort( header.getMessageType().toShort() );
        stream.writeInt( header.getMessageLength() );
    }
}
