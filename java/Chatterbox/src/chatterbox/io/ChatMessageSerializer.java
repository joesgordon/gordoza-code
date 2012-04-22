package chatterbox.io;

import java.io.IOException;
import java.util.*;

import org.jutils.io.IDataSerializer;
import org.jutils.io.IDataStream;

import chatterbox.data.DefaultMessage;
import chatterbox.model.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatMessageSerializer implements IDataSerializer<IChatMessage>
{
    /**  */
    private StringSerializer stringSerializer;
    /**  */
    private UserSerializer userSerializer;
    /**  */
    private MessageAttributeSetSerializer attributeSerializer;
    /**  */
    private IUser localUser;

    /***************************************************************************
     * @param localUser
     **************************************************************************/
    public ChatMessageSerializer( IUser localUser )
    {
        stringSerializer = new StringSerializer();
        userSerializer = new UserSerializer();
        this.localUser = localUser;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IChatMessage read( IDataStream stream ) throws IOException
    {
        String conversationId;
        long sendTime;
        IUser sender;
        String text;
        int numAttributes;
        List<IMessageAttributeSet> attributeSets = new ArrayList<IMessageAttributeSet>();

        conversationId = stringSerializer.read( stream );
        sendTime = stream.readLong();
        sender = userSerializer.read( stream );
        text = stringSerializer.read( stream );
        numAttributes = stream.readInt();
        for( int i = 0; i < numAttributes; i++ )
        {
            attributeSets.add( attributeSerializer.read( stream ) );
        }

        return new DefaultMessage( sender, text, attributeSets, new Date(
            sendTime ), conversationId, localUser.equals( sender ) );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void write( IChatMessage message, IDataStream stream )
        throws IOException
    {
        List<IMessageAttributeSet> attributes = message.getAttributeSets();

        stringSerializer.write( message.getConversationId(), stream );
        stream.writeLong( message.getTime().getTime() );
        userSerializer.write( message.getSender(), stream );
        stringSerializer.write( message.getText(), stream );
        stream.writeInt( message.getAttributeSets().size() );
        for( IMessageAttributeSet attribute : attributes )
        {
            attributeSerializer.write( attribute, stream );
        }
    }
}
