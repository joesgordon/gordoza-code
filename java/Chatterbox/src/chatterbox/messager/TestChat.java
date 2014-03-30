package chatterbox.messager;

import java.util.List;

import org.jutils.io.LogUtils;

import chatterbox.data.ChatUser;
import chatterbox.model.IConversation;
import chatterbox.model.IUser;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TestChat extends AbstractChat
{
    /**  */
    private IConversation defaultConversation;

    /***************************************************************************
     * 
     **************************************************************************/
    public TestChat()
    {
        defaultConversation = new TestConversation( this, "", null );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void connect( String group, int port )
    {
        IUser user;

        user = new ChatUser( "fflinstone", "Fred Flinstone" );
        defaultConversation.addUser( user );

        user = new ChatUser( "brubble", "Barney Rubble" );
        user.setAvailable( false );
        defaultConversation.addUser( user );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IConversation createConversation( List<IUser> users )
    {
        String id = getNextConversationId();
        IConversation conv = new TestConversation( this, id, users );

        addConversation( conv );

        LogUtils.printDebug( id );

        return conv;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public IConversation getDefaultConversation()
    {
        return defaultConversation;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void disconnect()
    {
        ;
    }

    @Override
    public String getAddress()
    {
        return "?";
    }

    @Override
    public int getPort()
    {
        return -1;
    }
}
