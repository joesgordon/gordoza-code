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

    /***************************************************************************
     * Application entry point.
     * @param args String[]
     **************************************************************************/
    public static void main( String [] args )
    {
        IFrameApp app = new FileSpyApp( createUserIO() );
        FrameApplication.invokeLater( app );
    }

    /***************************************************************************
     * 
     **************************************************************************/
    private static class OptionsCreator implements
        IOptionsCreator<FileSpyData>
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

    public static OptionsSerializer<FileSpyData> createUserIO()
    {
        OptionsSerializer<FileSpyData> userio;
        userio = OptionsSerializer.getUserIO( new OptionsCreator(),
            USER_OPTIONS_FILE );

        return userio;
    }
}
