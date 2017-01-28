package chatterbox;

import org.jutils.ui.app.FrameRunner;

import chatterbox.data.ChatUser;
import chatterbox.messenger.Chat;

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
        ChatUser user = new ChatUser( "jose", "gordoza" );

        FrameRunner.invokeLater( new ChatterboxApp( new Chat( user ) ) );
    }
}
