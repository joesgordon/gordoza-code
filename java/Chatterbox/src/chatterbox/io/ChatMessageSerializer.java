package chatterbox.io;

import java.io.IOException;
import java.util.*;

import org.jutils.io.IDataSerializer;
import org.jutils.io.IDataStream;

import chatterbox.model.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatMessageSerializer implements IDataSerializer<ChatMessage>
{
    /**  */
    private StringSerializer stringSerializer;
    /**  */
    private UserSerializer userSerializer;
    /**  */
    private MessageAttributeSetSerializer attributeSerializer;

    /***************************************************************************
     * @param localUser
     **************************************************************************/
    public ChatMessageSerializer()
    {
        stringSerializer = new StringSerializer();
        userSerializer = new UserSerializer();
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public ChatMessage read( IDataStream stream ) throws IOException
    {
        String conversationId;
        long txTime;
        long rxTime = new GregorianCalendar(
            TimeZone.getTimeZone( "UTC" ) ).getTimeInMillis();
        IUser sender;
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
        stream.writeLong( message.txTime );
        userSerializer.write( message.sender, stream );
        stringSerializer.write( message.text, stream );
        stream.writeInt( message.attributes.size() );

        for( MessageAttributeSet attribute : message.attributes )
        {
            attributeSerializer.write( attribute, stream );
        }
    }
}
