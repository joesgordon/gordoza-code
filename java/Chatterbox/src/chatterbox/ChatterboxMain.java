package chatterbox;

import org.jutils.io.LogUtils;
import org.jutils.io.options.OptionsSerializer;
import org.jutils.ui.app.FrameRunner;

import chatterbox.data.ChatUser;
import chatterbox.data.ChatterConfig;
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
        OptionsSerializer<ChatterConfig> options = ChatterboxConstants.getOptions();
        ChatUser user = ChatterboxConstants.createDefaultUser();

        LogUtils.printDebug(
            "Default ip: " + options.getOptions().chatCfg.address.toString() );

        user.displayName = options.getOptions().displayName;

        FrameRunner.invokeLater( new ChatterboxApp( new Chat( user ) ) );
    }
}
