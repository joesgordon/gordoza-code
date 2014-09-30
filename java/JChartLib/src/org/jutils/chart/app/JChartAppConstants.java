package org.jutils.chart.app;

import java.io.File;

import org.jutils.io.IOUtils;
import org.jutils.io.UserOptionsSerializer;

/*******************************************************************************
 * 
 ******************************************************************************/
public class JChartAppConstants
{
    /**  */
    public static final String APP_NAME = "JChart";
    /**  */
    public static final File USER_APP_DIR = IOUtils.getUsersFile( ".jchart" );
    /**  */
    private static final File USER_OPTIONS_FILE = IOUtils.getUsersFile(
        ".jutils", "jchart", "options.xml" );

    /**  */
    private static UserOptionsSerializer<UserData> userio;

    /***************************************************************************
     * 
     **************************************************************************/
    private JChartAppConstants()
    {
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static UserOptionsSerializer<UserData> getUserIO()
    {
        if( userio == null )
        {
            userio = UserOptionsSerializer.getUserIO( UserData.class,
                USER_OPTIONS_FILE );
        }

        return userio;
    }
}
