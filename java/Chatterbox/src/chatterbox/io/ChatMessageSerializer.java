package chatterbox.io;

import java.io.IOException;

import org.jutils.data.TextStyleList;
import org.jutils.io.IDataSerializer;
import org.jutils.io.IDataStream;

import chatterbox.ChatterboxConstants;
import chatterbox.data.DecoratedText;
import chatterbox.data.messages.ChatMessage;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatMessageSerializer implements IDataSerializer<ChatMessage>
{
    /**  */
    private final StringSerializer stringSerializer;
    /**  */
    private final TextStyleListSerializer attributeSerializer;

    /***************************************************************************
     * @param localUser
     **************************************************************************/
    public ChatMessageSerializer()
    {
        this.stringSerializer = new StringSerializer();
        this.attributeSerializer = new TextStyleListSerializer();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public ChatMessage read( IDataStream stream ) throws IOException
    {
        String chatId;
        long txTime;
        long rxTime = ChatterboxConstants.now();
        String sender;

        chatId = stringSerializer.read( stream );
        txTime = stream.readLong();
        sender = stringSerializer.read( stream );
        String chars = stringSerializer.read( stream );
        TextStyleList styles = attributeSerializer.read( stream );
        DecoratedText text = new DecoratedText( chars, styles );

        ChatMessage msg = new ChatMessage( chatId, sender, txTime, rxTime,
            text );

        return msg;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( ChatMessage message, IDataStream stream )
        throws IOException
    {
        stringSerializer.write( message.chatId, stream );
        stream.writeLong( ChatterboxConstants.now() );
        stringSerializer.write( message.sender, stream );
        stringSerializer.write( message.text.text, stream );
        attributeSerializer.write( message.text.attributes, stream );
    }
}
