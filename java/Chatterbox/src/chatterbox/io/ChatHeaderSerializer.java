package chatterbox.io;

import java.io.IOException;

import org.jutils.io.*;

import chatterbox.data.ChatHeader;
import chatterbox.data.ChatMessageType;

public class ChatHeaderSerializer implements IDataSerializer<ChatHeader>
{
    private static final long HEADER_MAGiC_NUM = 0x20FE90FA286BEB40L;

    @Override
    public ChatHeader read( IDataStream stream )
        throws IOException, RuntimeFormatException
    {
        if( stream.getAvailable() < 14 )
        {
            throw new RuntimeFormatException(
                "Message length too short: " + stream.getAvailable() );
        }

        long num;
        short msgTypeNum;
        ChatMessageType messageType;
        int length;

        num = stream.readLong();

        if( num != HEADER_MAGiC_NUM )
        {
            throw new RuntimeFormatException(
                "A non-Chatterbox message was received." );
        }

        msgTypeNum = stream.readShort();
        try
        {
            messageType = ChatMessageType.fromShort( msgTypeNum );
        }
        catch( IllegalArgumentException ex )
        {
            throw new RuntimeFormatException(
                "Invalid message type: " + msgTypeNum, ex );
        }
        length = stream.readInt();

        return new ChatHeader( messageType, length );
    }

    @Override
    public void write( ChatHeader header, IDataStream stream )
        throws IOException, RuntimeFormatException
    {
        stream.writeLong( HEADER_MAGiC_NUM );
        stream.writeShort( header.getMessageType().toShort() );
        stream.writeInt( header.getMessageLength() );
    }
}
