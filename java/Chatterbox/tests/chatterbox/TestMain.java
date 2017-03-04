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
        ChatUser user = new ChatUser( "jose", "gordoza" );

        FrameRunner.invokeLater( new ChatterboxApp( user ) );
    }
}
