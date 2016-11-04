package org.jutils.apps.filespy;

import java.io.File;

import org.jutils.apps.filespy.data.FileSpyData;
import org.jutils.io.IOUtils;
import org.jutils.io.LogUtils;
import org.jutils.io.options.IOptionsCreator;
import org.jutils.io.options.OptionsSerializer;
import org.jutils.ui.app.FrameRunner;
import org.jutils.ui.app.IFrameApp;

/*******************************************************************************
 *
 ******************************************************************************/
public class FileSpyMain
{
    /**  */
    public static final File USER_OPTIONS_FILE = IOUtils.getUsersFile(
        ".jutils", "filespy", "options.xml" );

    /**  */
    private static final OptionsSerializer<FileSpyData> OPTIONS;

    static
    {
        OPTIONS = OptionsSerializer.getOptions( new OptionsCreator(),
            USER_OPTIONS_FILE );
    }

    /***************************************************************************
     * Application entry point.
     * @param args String[]
     **************************************************************************/
    public static void main( String [] args )
    {
        IFrameApp app = new FileSpyApp();
        FrameRunner.invokeLater( app );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static OptionsSerializer<FileSpyData> getOptions()
    {
        return OPTIONS;
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class OptionsCreator implements IOptionsCreator<FileSpyData>
    {
        @Override
        public FileSpyData createDefaultOptions()
        {
            FileSpyData data = new FileSpyData();

            return data;
        }

        @Override
        public FileSpyData initialize( FileSpyData item_read )
        {
            return new FileSpyData( item_read );
        }

        @Override
        public void warn( String message )
        {
            LogUtils.printWarning( message );
        }
    }
}
