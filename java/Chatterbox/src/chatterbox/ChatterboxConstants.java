package chatterbox;

import java.io.File;
import java.util.GregorianCalendar;
import java.util.TimeZone;

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
    public static final TimeZone UTC = TimeZone.getTimeZone( "UTC" );

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

    /***************************************************************************
     * @return
     **************************************************************************/
    public static long now()
    {
        return new GregorianCalendar().getTimeInMillis();
    }
}
