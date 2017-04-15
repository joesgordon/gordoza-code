package chatterbox;

import java.awt.Image;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

import org.jutils.IconConstants;
import org.jutils.data.SystemProperty;
import org.jutils.io.IOUtils;
import org.jutils.io.options.OptionsSerializer;

import chatterbox.data.ChatUser;
import chatterbox.data.ChatterboxOptions;

/*******************************************************************************
 * 
 ******************************************************************************/
public class ChatterboxConstants
{
    /**  */
    private static final File USER_FILE = IOUtils.getUsersFile( ".jutils",
        "chatterbox", "options.xml" );
    /**  */
    public static final String DEFAULT_USERNAME = SystemProperty.USER_NAME.getProperty();
    /**  */
    public static final TimeZone UTC = TimeZone.getTimeZone( "UTC" );

    /**  */
    private static OptionsSerializer<ChatterboxOptions> options;

    /***************************************************************************
     * @return
     **************************************************************************/
    public static OptionsSerializer<ChatterboxOptions> getOptions()
    {
        if( options == null )
        {
            options = OptionsSerializer.getOptions( ChatterboxOptions.class,
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
    public static ChatUser createDefaultUser()
    {
        String system = getHostname();
        String username = DEFAULT_USERNAME;
        String userId = username + "@" + system;
        ChatUser user = new ChatUser( userId );

        user.displayName = getOptions().getOptions().displayName;

        return user;
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static String getHostname()
    {
        Map<String, String> env = System.getenv();

        if( env.containsKey( "COMPUTERNAME" ) )
        {
            return env.get( "COMPUTERNAME" );
        }
        else if( env.containsKey( "HOSTNAME" ) )
        {
            return env.get( "HOSTNAME" );
        }
        else
        {
            try
            {
                InetAddress addr = InetAddress.getLocalHost();
                return addr.getHostName();
            }
            catch( UnknownHostException ex )
            {
                try
                {
                    ProcessBuilder pb = new ProcessBuilder( "hostname" );
                    Process process = pb.start();
                    String host = null;

                    try( InputStream is = process.getInputStream();
                         InputStreamReader isr = new InputStreamReader( is );
                         BufferedReader br = new BufferedReader( isr ) )
                    {
                        host = br.readLine();
                    }

                    process.waitFor();

                    if( host == null )
                    {
                        return "Unknown Computer";
                    }

                    return host.trim();
                }
                catch( IOException | InterruptedException e )
                {
                    return "Unknown Computer";
                }
            }
        }
    }
}
