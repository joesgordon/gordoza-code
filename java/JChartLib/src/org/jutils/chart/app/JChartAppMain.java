package org.jutils.chart.app;

import java.io.File;

import org.jutils.io.IOUtils;
import org.jutils.io.UserOptionsSerializer;
import org.jutils.ui.app.FrameApplication;

/*******************************************************************************
 * 
 ******************************************************************************/
public class JChartAppMain
{
    /**  */
    private static final File USER_OPTIONS_FILE = IOUtils.getUsersFile(
        ".jutils", "jchart", "options.xml" );

    /***************************************************************************
     * @param args
     **************************************************************************/
    public static void main( String [] args )
    {
        UserOptionsSerializer<UserData> userio;

        userio = UserOptionsSerializer.getUserIO( UserData.class,
            USER_OPTIONS_FILE );

        JChartApp runnable = new JChartApp( userio );

        FrameApplication.invokeLater( runnable );
    }
}
