package chatterbox.messenger;

import java.util.List;

import org.jutils.io.LogUtils;

import chatterbox.data.ChatConfig;
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
    /**  */
    private ChatConfig config;

    /***************************************************************************
     * 
     **************************************************************************/
    public TestChat()
    {
        super( new ChatUser( System.getProperty( "user.name" ) ) );
        defaultConversation = new TestConversation( this, "", null );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void connect( ChatConfig config )
    {
        this.config = config;

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

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public ChatConfig getConfig()
    {
        return config;
    }
}
