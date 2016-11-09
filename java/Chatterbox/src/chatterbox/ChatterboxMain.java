package chatterbox;

import org.jutils.ui.app.FrameRunner;

import chatterbox.data.ChatUser;
import chatterbox.messenger.Chat;
import chatterbox.model.IUser;

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
        IUser user;

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
