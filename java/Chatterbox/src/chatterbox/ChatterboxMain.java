package chatterbox;

import org.jutils.ui.app.FrameRunner;

import chatterbox.data.ChatUser;

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
        ChatUser user = ChatterboxConstants.createDefaultUser();
        FrameRunner.invokeLater( new ChatterboxApp( user ) );
    }
}
