package chatterbox;

import org.jutils.ui.app.FrameRunner;

import chatterbox.data.ChatUser;

/*******************************************************************************
 * 
 ******************************************************************************/
public class TestMain
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String[] args )
    {
        ChatUser user;

        user = createUser( "jose", "gordoza" );
        FrameRunner.invokeLater( new ChatterboxApp( user ) );

        user = ChatterboxConstants.createDefaultUser();
        FrameRunner.invokeLater( new ChatterboxApp( user ) );
    }

    private static ChatUser createUser( String username, String displayName )
    {
        String system = ChatterboxConstants.getHostname();
        String userId = username + "@" + system;
        return new ChatUser( userId, displayName );
    }
}
