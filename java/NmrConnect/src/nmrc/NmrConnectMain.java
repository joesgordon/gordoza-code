package nmrc;

import java.awt.Dimension;
import java.io.File;

import javax.swing.JFrame;

import nmrc.controller.NmrConnectController;
import nmrc.data.NmrcUserPrefs;
import nmrc.ui.NmrConnectFrame;

import org.jutils.Utils;
import org.jutils.io.ConfigFile;
import org.jutils.ui.app.FrameApplication;
import org.jutils.ui.app.IFrameApp;

/*******************************************************************************
 *
 ******************************************************************************/
public class NmrConnectMain implements IFrameApp
{
    /**  */
    public static final String OPTIONS_FILE_NAME = "ctdOptions.cfg";

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public JFrame createFrame()
    {
        File cfgFile = new File( Utils.USER_HOME + File.separator +
            OPTIONS_FILE_NAME );

        ConfigFile<String, String> configData = new ConfigFile<String, String>(
            cfgFile );

        NmrConnectFrame frame = new NmrConnectFrame( new NmrcUserPrefs(
            configData ) );
        // String lastFolderPath = configData.get( DEFAULT_FOLDER_KEY );
        // File lastFolder = lastFolderPath != null ? new File(
        // lastFolderPath ) : null;
        // frame.setLastFolder( lastFolder );

        new NmrConnectController( frame );

        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setSize( new Dimension( 900, 600 ) );

        return frame;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    @Override
    public void finalizeGui()
    {
    }

    /***************************************************************************
     * Application entry point.
     * @param args String[]
     **************************************************************************/
    public static void main( String[] args )
    {
        FrameApplication.invokeLater( new NmrConnectMain() );
    }
}
