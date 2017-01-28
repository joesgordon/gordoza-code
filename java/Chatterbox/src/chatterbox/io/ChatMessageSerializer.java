package chatterbox.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jutils.io.IDataSerializer;
import org.jutils.io.IDataStream;

import chatterbox.ChatterboxConstants;
import chatterbox.data.ChatUser;
import chatterbox.model.ChatMessage;
import chatterbox.model.MessageAttributeSet;

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
    private final MessageAttributeSetSerializer attributeSerializer;

    /***************************************************************************
     * @param localUser
     **************************************************************************/
    public ChatMessageSerializer()
    {
        this.stringSerializer = new StringSerializer();
        this.userSerializer = new UserSerializer();
        this.attributeSerializer = new MessageAttributeSetSerializer();
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
        String text;
        int numAttributes;
        List<MessageAttributeSet> attributeSets = new ArrayList<MessageAttributeSet>();

        conversationId = stringSerializer.read( stream );
        txTime = stream.readLong();
        sender = userSerializer.read( stream );
        text = stringSerializer.read( stream );
        numAttributes = stream.readInt();
        for( int i = 0; i < numAttributes; i++ )
        {
            attributeSets.add( attributeSerializer.read( stream ) );
        }

        ChatMessage msg = new ChatMessage( conversationId, sender, txTime,
            rxTime, text, attributeSets );

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
        stringSerializer.write( message.text, stream );
        stream.writeInt( message.attributes.size() );

        for( MessageAttributeSet attribute : message.attributes )
        {
            attributeSerializer.write( attribute, stream );
        }
    }
}
