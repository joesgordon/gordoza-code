package org.jutils.apps.filespy;

import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.jutils.apps.filespy.data.FileSpyData;
import org.jutils.io.IOUtils;
import org.jutils.ui.FrameRunner;
import org.jutils.ui.StandardUncaughtExceptionHandler;

/*******************************************************************************
 *
 ******************************************************************************/
public class FileSpyMain extends FrameRunner
{
    /**  */
    public static final String CONFIG_FILENAME = ".fileSpy.conf";
    /**  */
    private static FileSpyData configData = null;

    /***************************************************************************
     * @return FileSpyData
     **************************************************************************/
    public static FileSpyData getConfigData()
    {
        if( configData == null )
        {
            File file = new File( IOUtils.USERS_DIR, CONFIG_FILENAME );
            try
            {
                configData = ( FileSpyData )FileSpyData.read( file );
            }
            catch( IOException ex )
            {
                configData = new FileSpyData();
                configData.setFile( file );
            }
        }

        return configData;
    }

    /***************************************************************************
     * Construct and show the application.
     **************************************************************************/
    @Override
    protected JFrame createFrame()
    {
        FileSpyFrame frame = new FileSpyFrame();
        StandardUncaughtExceptionHandler fsue = new StandardUncaughtExceptionHandler(
            frame );
        Thread.setDefaultUncaughtExceptionHandler( fsue );

        return frame;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    protected boolean validate()
    {
        return true;
    }

    /***************************************************************************
     * Application entry point.
     * @param args String[]
     **************************************************************************/
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( new FileSpyMain() );
    }
}
