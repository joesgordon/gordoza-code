package org.jutils.apps.filespy;

import java.io.File;

import org.jutils.apps.filespy.data.FileSpyData;
import org.jutils.io.*;
import org.jutils.io.OptionsSerializer.IOptionsCreator;
import org.jutils.ui.app.FrameApplication;
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
    private static OptionsSerializer<FileSpyData> options;

    /***************************************************************************
     * Application entry point.
     * @param args String[]
     **************************************************************************/
    public static void main( String [] args )
    {
        IFrameApp app = new FileSpyApp();
        FrameApplication.invokeLater( app );
    }

    /***************************************************************************
     * @return
     **************************************************************************/
    public static OptionsSerializer<FileSpyData> getOptions()
    {
        if( options == null )
        {
            options = OptionsSerializer.getOptions( new OptionsCreator(),
                USER_OPTIONS_FILE );
        }

        return options;
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
