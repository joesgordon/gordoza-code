package chatterbox;

import java.awt.Image;
import java.io.File;
import java.util.*;

import org.jutils.IconConstants;
import org.jutils.io.IOUtils;
import org.jutils.io.options.OptionsSerializer;

import chatterbox.data.ChatUser;
import chatterbox.data.ChatterConfig;
import chatterbox.model.IUser;

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
    private static OptionsSerializer<ChatterConfig> options;

    /***************************************************************************
     * @return
     **************************************************************************/
    public static OptionsSerializer<ChatterConfig> getOptions()
    {
        if( options == null )
        {
            options = OptionsSerializer.getOptions( ChatterConfig.class,
                USER_FILE );
        }

        return options;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static long now()
    {
        return new GregorianCalendar().getTimeInMillis();
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static List<? extends Image> getIcons()
    {
        return IconConstants.getImages( IconConstants.CHAT_16,
            IconConstants.CHAT_32, IconConstants.CHAT_48,
            IconConstants.CHAT_64 );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static IUser createDefaultUser()
    {
        return new ChatUser( System.getProperty( "user.name" ) );
    }
}
