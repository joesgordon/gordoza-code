package chatterbox;

import java.io.File;

import org.jutils.io.IOUtils;
import org.jutils.io.OptionsSerializer;

import chatterbox.data.ChatterConfig;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatterboxConstants
{
    /**  */
    private static final File USER_FILE = IOUtils.getUsersFile( "jutils",
        "chatterbox", "options.xml" );
    /**  */
    public static final String DEFAULT_USERNAME = System.getProperty(
        "user.name" );

    /**  */
    private static OptionsSerializer<ChatterConfig> userio;

    /***************************************************************************
     * @return
     **************************************************************************/
    public static OptionsSerializer<ChatterConfig> getUserIO()
    {
        if( userio == null )
        {
            userio = OptionsSerializer.getUserIO( ChatterConfig.class,
                USER_FILE );
        }

        return userio;
    }
}
