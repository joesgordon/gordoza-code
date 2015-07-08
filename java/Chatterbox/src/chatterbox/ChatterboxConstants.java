package chatterbox;

import java.io.File;

import org.jutils.io.IOUtils;
import org.jutils.io.OptionsSerializer;

import chatterbox.data.ChatConfig;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatterboxConstants
{
    private static final File USER_FILE = IOUtils.getUsersFile( "jutils",
        "chatterbox", "options.xml" );
    public static final String DEFAULT_USERNAME = System.getProperty( "user.name" );
    private static OptionsSerializer<ChatConfig> userio;

    /***************************************************************************
     * @return
     **************************************************************************/
    public static OptionsSerializer<ChatConfig> getUserIO()
    {
        if( userio == null )
        {
            userio = OptionsSerializer.getUserIO( ChatConfig.class, USER_FILE );
        }

        return userio;
    }
}
