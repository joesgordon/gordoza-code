package chatterbox;

import org.jutils.ui.app.FrameRunner;

import chatterbox.data.ChatUser;
import chatterbox.messenger.Chat;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatterboxMain
{
    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String[] args )
    {
        ChatUser user;

        if( args.length == 1 )
        {
            user = new ChatUser( args[0] );
        }
        else
        {
            user = ChatterboxConstants.createDefaultUser();
        }

        FrameRunner.invokeLater( new ChatterboxApp( new Chat( user ) ) );
    }
}
