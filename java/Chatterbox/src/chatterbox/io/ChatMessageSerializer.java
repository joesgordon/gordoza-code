package chatterbox.io;

import java.io.IOException;

import org.jutils.io.IDataSerializer;
import org.jutils.io.IDataStream;

import chatterbox.ChatterboxConstants;
import chatterbox.data.ChatUser;
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
    private final UserSerializer userSerializer;
    /**  */
    private final AttributeSetSerializer attributeSerializer;

    /***************************************************************************
     * @param localUser
     **************************************************************************/
    public ChatMessageSerializer()
    {
        this.stringSerializer = new StringSerializer();
        this.userSerializer = new UserSerializer();
        this.attributeSerializer = new AttributeSetSerializer();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public ChatMessage read( IDataStream stream ) throws IOException
    {
        String conversationId;
        long txTime;
        long rxTime = ChatterboxConstants.now();
        ChatUser sender;
        DecoratedText text = new DecoratedText();

        conversationId = stringSerializer.read( stream );
        txTime = stream.readLong();
        sender = userSerializer.read( stream );
        text.text = stringSerializer.read( stream );
        text.attributes = attributeSerializer.read( stream );

        ChatMessage msg = new ChatMessage( conversationId, sender, txTime,
            rxTime, text );

        return msg;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( ChatMessage message, IDataStream stream )
        throws IOException
    {
        stringSerializer.write( message.conversation, stream );
        stream.writeLong( ChatterboxConstants.now() );
        userSerializer.write( message.sender, stream );
        stringSerializer.write( message.text.text, stream );
        attributeSerializer.write( message.text.attributes, stream );
    }
}
