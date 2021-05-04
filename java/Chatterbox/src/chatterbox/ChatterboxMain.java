package chatterbox;

import org.jutils.core.ui.app.FrameRunner;

import chatterbox.data.ChatUser;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class ChatterboxMain
{
    /***************************************************************************
     * 
     **************************************************************************/
    private ChatterboxMain()
    {
    }

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String[] args )
    {
        ChatUser user = ChatterboxConstants.createDefaultUser();
        FrameRunner.invokeLater( new ChatterboxApp( user ) );
    }
}
